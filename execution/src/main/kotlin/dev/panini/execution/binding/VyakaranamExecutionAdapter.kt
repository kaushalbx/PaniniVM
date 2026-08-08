package dev.panini.execution.binding

import dev.panini.core.Karaka
import dev.panini.core.Lakara
import dev.panini.dhatupatha.Dhatu
import dev.panini.execution.DhatuInvocation
import dev.panini.execution.ExecutableUkti
import dev.panini.execution.ExecutionBindingResult
import dev.panini.execution.ExecutionExpression
import dev.panini.execution.ExecuteConditional
import dev.panini.execution.ExecuteInvocation
import dev.panini.execution.ExecuteRepeat
import dev.panini.execution.ExecuteSequence
import dev.panini.execution.ExecutionNode
import dev.panini.execution.KriyaInvocationId
import dev.panini.execution.GrammaticalFeatures
import dev.panini.execution.Polarity
import dev.panini.execution.SambhashanaContext
import dev.panini.execution.SanskritUktiInput
import dev.panini.execution.VakyaPrayojana
import dev.panini.execution.bindingName
import dev.panini.execution.memory.KriyaMemory
import dev.panini.analysis.PadaAnalyzer
import dev.panini.analysis.KriyaQualificationKind
import dev.panini.analysis.UktiAnalysis
import dev.panini.analysis.UktiAnalyzer
import dev.panini.analysis.VakyaAnalyzer
import dev.panini.vyakaranam.ast.AkhyataVakya
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.Conditional
import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.Pipeline
import dev.panini.vyakaranam.ast.ProgramNode
import dev.panini.vyakaranam.ast.Procedure
import dev.panini.vyakaranam.ast.Repeat
import dev.panini.vyakaranam.ast.SankhyaAbhyasaPada
import dev.panini.vyakaranam.ast.Sequence
import dev.panini.vyakaranam.ast.Scope
import dev.panini.vyakaranam.ast.TingantaPada
import dev.panini.vyakaranam.ast.Ukti
import dev.panini.vyakaranam.ast.expandedInvocations
import dev.panini.vyakaranam.lexicon.PratipadikaEntry
import dev.panini.vyakaranam.lexicon.VyakaranamLexicon
import dev.panini.vyakaranam.parser.PaniniParseException
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.collections.plusAssign

/**
 * Thin bridge from canonical vyākaraṇa analysis to execution semantics.
 * Grammatical case-to-kāraka policy remains owned by the vyākaraṇa package.
 *
 * Delegates to specialised internal objects:
 * - [DhatuCache]         — dhātu lookup and root resolution
 * - [ExpressionBuilder]  — SubantaPada → ExecutionExpression conversion
 * - [FrequencyExtractor] — repetition count extraction
 * - [KarakaExtractor]    — kāraka binding extraction
 */
object VyakaranamExecutionAdapter {
    private val parser = PaniniParser()

    internal fun analyzeForMemory(text: String): UktiAnalysis? {
        val ukti = try {
            parser.parse(text)
        } catch (_: PaniniParseException) {
            return null
        }
        if (ukti.vakyas.filterIsInstance<AkhyataVakya>().any { DhatuCache.resolve(it.tinganta) == null }) {
            return null
        }
        return analyze(ukti)
    }

    private fun analyze(ukti: Ukti): UktiAnalysis = UktiAnalyzer { vakya, frameId ->
        val akhyata = vakya as? AkhyataVakya
        if (akhyata == null) {
            VakyaAnalyzer(
                PadaAnalyzer(
                    object : VyakaranamLexicon {
                        override fun findPratipadika(text: String): PratipadikaEntry? = null
                        override fun findDhatu(text: String): Dhatu? = null
                    },
                ),
            ).analyze(vakya, frameId)
        } else {
            val dhatu = requireNotNull(DhatuCache.resolve(akhyata.tinganta))
            VakyaAnalyzer(
                PadaAnalyzer(
                    object : VyakaranamLexicon {
                        override fun findPratipadika(text: String): PratipadikaEntry? = null
                        override fun findDhatu(text: String): Dhatu = dhatu
                    },
                    validatePadaCompatibility = false,
                ),
            ).analyze(vakya, frameId)
        }
    }.analyze(ukti)

    fun bind(
        input: SanskritUktiInput,
        conversation: SambhashanaContext,
        memory: KriyaMemory = KriyaMemory(),
    ): ExecutionBindingResult {
        if (input.text.isBlank()) return ExecutionBindingResult.Invalid("The Sanskrit utterance is empty.")
        val ukti = try {
            parser.parse(input.text)
        } catch (e: PaniniParseException) {
            return ExecutionBindingResult.Invalid(e.message ?: "Invalid annotated Sanskrit morphology.")
        }

        var listener = input.listener
        ukti.sambodhana?.subanta?.pratipadika?.baseText()?.let { addressed ->
            if (!input.listener.startsWith(addressed)) listener = addressed
        }

        if (input.speaker != conversation.speaker) {
            return ExecutionBindingResult.Invalid("Utterance speaker does not match the trusted conversation context.")
        }
        val unresolved = ukti.vakyas.filterIsInstance<AkhyataVakya>()
            .firstOrNull { DhatuCache.resolve(it.tinganta) == null }
        if (unresolved != null) {
            return ExecutionBindingResult.Invalid(
                "Unknown verbal action/dhātu: ${unresolved.tinganta.sourceText}",
            )
        }
        val utteranceAnalysis = analyze(ukti)

        val invocations = mutableListOf<DhatuInvocation>()
        val qualificationKinds = utteranceAnalysis.frames
            .flatMap { it.qualifications }
            .mapTo(mutableSetOf()) { it.kind }
        val prayer = KriyaQualificationKind.COURTESY in qualificationKinds
        val prohibition = KriyaQualificationKind.NEGATION in qualificationKinds
        val localVariables = mutableSetOf<String>()
        val localVariableInvocationIds = mutableMapOf<String, String>()

        // An explicit abhyāsa count applies to the complete utterance.
        val abhyasaCounts = ukti.vakyas.flatMap { vakya ->
            vakya.padas.filterIsInstance<SankhyaAbhyasaPada>().mapNotNull { pada ->
                val numStems = FrequencyExtractor.numericStems(pada.stems)
                val evaluated = if (numStems.isNotEmpty()) {
                    sharedSankhyaEvaluator.evaluateStems(numStems)
                } else {
                    sharedSankhyaEvaluator.evaluateStems(pada.stems)
                }
                evaluated.value.toInt().takeIf { it > 0 }
            }
        }
        val executionBody = abhyasaCounts.maxOrNull()?.let { count ->
            Repeat(ukti.body.sourceText, count, ukti.body)
        } ?: lowerFrequencyQualifiers(ukti.body, utteranceAnalysis)
        val executionVakyas = executionBody.expandedInvocations().map(Invocation::vakya)

        executionVakyas.forEachIndexed { index, vakya ->
            val padas = vakya.padas
            val tinganta = (vakya as? AkhyataVakya)?.tinganta
            val dhatu = if (tinganta != null) {
                DhatuCache.resolve(tinganta)
                    ?: return ExecutionBindingResult.Invalid("Unknown verbal action/dhātu: ${tinganta.sourceText}")
            } else {
                DhatuCache["असँ"]
                    ?: return ExecutionBindingResult.Invalid("Imputed copular action 'अस्' not registered in DhatuPatha.")
            }
            val frame = utteranceAnalysis.frames.firstOrNull { it.vakya == vakya } ?: return@forEachIndexed
            val ctx = BindingContext(
                conversation = conversation,
                clauseIndex = index,
                dhatu = dhatu,
                frame = frame,
                memory = memory,
                previousDhatus = invocations.map { it.dhatu },
                localVariables = localVariables,
                localVariableInvocationIds = localVariableInvocationIds,
            )
            val invocation = buildDhatuInvocation(
                index = index,
                padas = padas,
                ctx = ctx,
                dhatu = dhatu,
                tinganta = tinganta,
                listener = listener,
                prayer = prayer,
            )
            invocations += invocation
            val bindingKaraka = dhatu.operations.firstOrNull { it.resultBindingKaraka != null }?.resultBindingKaraka
            val bindingName = bindingKaraka?.let { invocation.bindings[it] }?.bindingName()
            if (bindingName != null) {
                localVariables.add(bindingName)
                localVariableInvocationIds[bindingName] = KriyaInvocationId.of(index + 1)
            }
        }

        val lakara = ukti.vakyas.filterIsInstance<AkhyataVakya>().firstOrNull()?.tinganta?.lakara
        val purpose = when {
            prohibition -> VakyaPrayojana.NISHEDHA
            prayer -> VakyaPrayojana.PRARTHANA
            lakara == Lakara.LOT || lakara == null -> VakyaPrayojana.AJNA
            else -> VakyaPrayojana.VIDHANA
        }
        if (invocations.isEmpty()) return ExecutionBindingResult.Invalid("No executable verbal action was identified.")
        if (listener != conversation.listener) {
            return ExecutionBindingResult.Invalid("Addressed listener does not match the trusted conversation context.")
        }
        return ExecutionBindingResult.Bound(
            ExecutableUkti(
                speaker = input.speaker,
                listener = listener,
                text = input.text,
                prayojana = purpose,
                polarity = if (prohibition) Polarity.NEGATIVE else Polarity.POSITIVE,
                lakara = lakara,
                invocations = invocations,
                control = buildExecutionControl(executionBody),
            ),
            listOf("Bound canonical vyākaraṇa AST with ${ukti.vakyas.size} clause(s) directly to execution."),
        )
    }

    private fun buildExecutionControl(root: ProgramNode): ExecutionNode {
        var nextInvocation = 1
        fun build(node: ProgramNode): ExecutionNode = when (node) {
            is Invocation -> ExecuteInvocation(KriyaInvocationId.of(nextInvocation++))
            is Sequence -> ExecuteSequence(node.statements.map(::build))
            is Conditional -> ExecuteConditional(
                condition = build(node.condition),
                consequent = build(node.consequent),
                alternate = node.alternate?.let(::build),
            )
            is Repeat -> ExecuteRepeat(List(node.count) { build(node.body) })
            is Pipeline -> error("Pipelines are executed through their semantic stage engine.")
            is Procedure -> error("Procedure declarations are registered before utterance binding.")
            is Scope -> error("Scope declarations are registered before utterance binding.")
        }
        return build(root)
    }

    private fun purposeRequiresListenerAsAgent(prayer: Boolean, lakara: Lakara): Boolean =
        prayer || lakara == Lakara.LOT

    /**
     * Extracts kāraka bindings for [padas] and constructs the [DhatuInvocation] for
     * clause number [index] (0-based).
     */
    private fun buildDhatuInvocation(
        index: Int,
        padas: List<Pada>,
        ctx: BindingContext,
        dhatu: Dhatu,
        tinganta: TingantaPada?,
        listener: String,
        prayer: Boolean,
    ): DhatuInvocation {
        val extracted = KarakaExtractor.extractKarakas(padas, ctx)
        val bindings = extracted.bindings.toMutableMap()
        if (tinganta != null && purposeRequiresListenerAsAgent(prayer, tinganta.lakara) && Karaka.KARTR !in bindings) {
            bindings[Karaka.KARTR] = ExecutionExpression.Pada(listener)
        }
        val metadataMap = buildMap {
            put(dev.panini.execution.ExecutionMetadata.DEFAULT_DHATU, dhatu.upadesha)
            put(dev.panini.execution.ExecutionMetadata.dhatu(KriyaInvocationId.of(index + 1)), dhatu.upadesha)
        }
        return DhatuInvocation(
            id = KriyaInvocationId.of(index + 1),
            dhatu = dhatu,
            bindings = bindings,
            selectedOperation = null,
            metadata = metadataMap,
            grammaticalFeatures = GrammaticalFeatures(
                upasargas = tinganta?.upasargas?.toSet() ?: emptySet(),
                sanadi = tinganta?.dhatu?.sanadiPratyayas?.toSet() ?: emptySet(),
                avyayas = padas.filterIsInstance<AvyayaPada>().mapTo(mutableSetOf()) { it.form },
                lakara = tinganta?.lakara ?: Lakara.LAT,
            ),
            ambiguousBindings = extracted.ambiguous,
            karakaTrace = extracted.trace,
        )
    }

    private fun lowerFrequencyQualifiers(
        root: ProgramNode,
        analysis: UktiAnalysis,
    ): ProgramNode {
        var frameIndex = 0
        fun lower(node: ProgramNode): ProgramNode = when (node) {
            is Invocation -> {
                val frame = analysis.frames[frameIndex++]
                val count = FrequencyExtractor.extractFrequencyCount(node.vakya.padas, frame)
                if (count != null && count > 1) Repeat(node.sourceText, count, node) else node
            }
            is Sequence -> node.copy(statements = node.statements.map(::lower))
            is Conditional -> node.copy(
                condition = lower(node.condition),
                consequent = lower(node.consequent),
                alternate = node.alternate?.let(::lower),
            )
            is Repeat -> node.copy(body = lower(node.body))
            is Pipeline -> node
            is Procedure -> node.copy(body = node.body.map(::lower))
            is Scope -> node.copy(body = node.body.map(::lower))
        }
        return lower(root)
    }
}
