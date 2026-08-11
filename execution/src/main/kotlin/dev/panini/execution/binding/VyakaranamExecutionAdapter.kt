package dev.panini.execution.binding

import dev.panini.core.Karaka
import dev.panini.core.Lakara
import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.derivation.SubantaDerivationRequest
import dev.panini.derivation.SubantaEngine
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
import dev.panini.execution.ValueEnvironment
import dev.panini.execution.bindingName
import dev.panini.execution.memory.KriyaMemory
import dev.panini.analysis.PadaAnalyzer
import dev.panini.analysis.SankhyaPadaValueResolver
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
import dev.panini.vyakaranam.ast.ProgramNodeTransformer
import dev.panini.vyakaranam.ast.ProgramNodeVisitor
import dev.panini.vyakaranam.ast.Procedure
import dev.panini.vyakaranam.ast.Quotation
import dev.panini.vyakaranam.ast.Repeat
import dev.panini.vyakaranam.ast.SankhyaAbhyasaPada
import dev.panini.vyakaranam.ast.Sequence
import dev.panini.vyakaranam.ast.Scope
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.TingantaPada
import dev.panini.vyakaranam.ast.Ukti
import dev.panini.vyakaranam.ast.Vakya
import dev.panini.vyakaranam.ast.WhileLoop
import dev.panini.vyakaranam.ast.expandedInvocations
import dev.panini.vyakaranam.ast.depthFirst
import dev.panini.vyakaranam.ast.accept
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
        if (ukti.grammaticalVakyas().filterIsInstance<AkhyataVakya>().any { DhatuCache.resolve(it.tinganta) == null }) {
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
                    sankhyaValueResolver = SankhyaPadaValueResolver(NumeralPadaBinder::resolveSemanticValue),
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
                    sankhyaValueResolver = SankhyaPadaValueResolver(NumeralPadaBinder::resolveSemanticValue),
                ),
            ).analyze(vakya, frameId)
        }
    }.analyze(ukti)

    fun bind(
        input: SanskritUktiInput,
        conversation: SambhashanaContext,
        memory: KriyaMemory = KriyaMemory(),
        environment: ValueEnvironment = ValueEnvironment(),
    ): ExecutionBindingResult {
        if (input.text.isBlank()) return ExecutionBindingResult.Invalid("The Sanskrit utterance is empty.")
        val ukti = try {
            parser.parse(input.text)
        } catch (e: PaniniParseException) {
            return ExecutionBindingResult.Invalid(e.message ?: "Invalid annotated Sanskrit morphology.")
        }
        val quotations = quotationBindings(ukti.body)
        val executableUkti = ukti

        var listener = input.listener
        executableUkti.sambodhana?.subanta?.pratipadika?.baseText()?.let { addressed ->
            if (!input.listener.startsWith(addressed)) listener = addressed
        }

        if (input.speaker != conversation.speaker) {
            return ExecutionBindingResult.Invalid("Utterance speaker does not match the trusted conversation context.")
        }
        val unresolved = executableUkti.grammaticalVakyas().filterIsInstance<AkhyataVakya>()
            .firstOrNull { DhatuCache.resolve(it.tinganta) == null }
        if (unresolved != null) {
            return ExecutionBindingResult.Invalid(
                "Unknown verbal action/dhātu: ${unresolved.tinganta.sourceText}",
            )
        }
        val utteranceAnalysis = analyze(executableUkti)

        val invocations = mutableListOf<DhatuInvocation>()
        val qualificationKinds = utteranceAnalysis.frames
            .flatMap { it.qualifications }
            .mapTo(mutableSetOf()) { it.kind }
        val prayer = KriyaQualificationKind.COURTESY in qualificationKinds
        val prohibition = KriyaQualificationKind.NEGATION in qualificationKinds
        val localVariables = mutableSetOf<String>()
        val localVariableInvocationIds = mutableMapOf<String, String>()

        // An explicit abhyāsa count applies to the complete utterance.
        val abhyasaCounts = executableUkti.grammaticalVakyas().flatMap { vakya ->
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
            Repeat(executableUkti.body.sourceText, count, executableUkti.body)
        } ?: lowerFrequencyQualifiers(executableUkti.body, utteranceAnalysis)
        val executionVakyas = executionBody.expandedInvocations().map(Invocation::vakya)
        val pipelineKarmanSources = pipelineKarmanSources(executionBody)

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
                environment = environment,
            )
            val invocation = buildDhatuInvocation(
                index = index,
                padas = padas,
                ctx = ctx,
                dhatu = dhatu,
                tinganta = tinganta,
                listener = listener,
                prayer = prayer,
                pipelineKarmanSource = pipelineKarmanSources[index + 1],
                quotedVakya = quotations[vakya],
            )
            invocations += invocation
            val bindingKaraka = dhatu.operations.firstOrNull { it.resultBindingKaraka != null }?.resultBindingKaraka
            val bindingName = bindingKaraka?.let { invocation.bindings[it] }?.bindingName()
            if (bindingName != null) {
                localVariables.add(bindingName)
                localVariableInvocationIds[bindingName] = KriyaInvocationId.of(index + 1)
            }
        }

        val lakara = executableUkti.grammaticalVakyas().filterIsInstance<AkhyataVakya>().firstOrNull()?.tinganta?.lakara
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
            listOf("Bound canonical vyākaraṇa AST with ${ukti.grammaticalVakyas().size} clause(s) directly to execution."),
        )
    }

    private fun buildExecutionControl(root: ProgramNode): ExecutionNode {
        var nextInvocation = 1
        val visitor = object : ProgramNodeVisitor<ExecutionNode> {
            private fun build(node: ProgramNode): ExecutionNode = node.accept(this)
            override fun visitInvocation(node: Invocation): ExecutionNode =
                ExecuteInvocation(KriyaInvocationId.of(nextInvocation++))
            override fun visitSequence(node: Sequence): ExecutionNode =
                ExecuteSequence(node.statements.map(::build))
            override fun visitConditional(node: Conditional): ExecutionNode = ExecuteConditional(
                condition = build(node.condition),
                consequent = build(node.consequent),
                alternate = node.alternate?.let(::build),
            )
            override fun visitQuotation(node: Quotation): ExecutionNode = build(node.reporting)
            override fun visitRepeat(node: Repeat): ExecutionNode =
                ExecuteRepeat(List(node.count) { build(node.body) })
            override fun visitWhileLoop(node: WhileLoop): ExecutionNode = build(node.body)
            override fun visitPipeline(node: Pipeline): ExecutionNode =
                error("Pipelines are executed through their semantic stage engine.")
            override fun visitProcedure(node: Procedure): ExecutionNode =
                error("Procedure declarations are registered before utterance binding.")
            override fun visitScope(node: Scope): ExecutionNode =
                error("Scope declarations are registered before utterance binding.")
        }
        return root.accept(visitor)
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
        pipelineKarmanSource: Int?,
        quotedVakya: Vakya?,
    ): DhatuInvocation {
        val extracted = KarakaExtractor.extractKarakas(padas, ctx)
        val bindings = extracted.bindings.toMutableMap()
        if (quotedVakya != null) {
            bindQuotation(quotedVakya, ctx).forEach { (karaka, expression) ->
                bindings.putIfAbsent(karaka, expression)
            }
        }
        if (pipelineKarmanSource != null && Karaka.KARMAN !in bindings) {
            bindings[Karaka.KARMAN] = ExecutionExpression.Reference(
                KriyaInvocationId.of(pipelineKarmanSource),
            )
        }
        if (tinganta != null && purposeRequiresListenerAsAgent(prayer, tinganta.lakara) && Karaka.KARTR !in bindings) {
            bindings[Karaka.KARTR] = ExecutionExpression.Pada(listener)
        }
        val metadataMap = buildMap {
            put(dev.panini.execution.ExecutionMetadata.DEFAULT_DHATU, dhatu.upadesha)
            put(dev.panini.execution.ExecutionMetadata.dhatu(KriyaInvocationId.of(index + 1)), dhatu.upadesha)
            if (quotedVakya != null) {
                put(dev.panini.execution.RENDER_ACTIVE_RANGE_METADATA, "true")
            }
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
            karakaTrace = extracted.trace + if (pipelineKarmanSource != null && Karaka.KARMAN in bindings &&
                Karaka.KARMAN !in extracted.bindings
            ) {
                listOf("Piped the preceding ततः result into the missing KARMAN binding.")
            } else {
                emptyList()
            },
        )
    }

    /** Converts the quoted command to printable operands without scheduling its verb. */
    private fun bindQuotation(vakya: Vakya, ctx: BindingContext): Map<Karaka, ExecutionExpression> {
        val extracted = KarakaExtractor.extractKarakas(vakya.padas, ctx)
        val transferable = extracted.bindings.filterKeys { it in setOf(Karaka.APADANA, Karaka.ADHIKARANA) }
        val words = vakya.padas.filterIsInstance<SubantaPada>()
            .filter { pada ->
                SupAffix.candidates(pada.sup.text).any { it.vibhakti == Vibhakti.DVITIYA }
            }
            .map { pada ->
                val stem = pada.pratipadika.baseText()
                val affix = SupAffix.candidates(pada.sup.text)
                    .first { it.vibhakti == Vibhakti.DVITIYA }
                val surface = runCatching {
                    SubantaEngine().derive(
                        SubantaDerivationRequest(stem, affix.vibhakti, affix.vacana),
                    ).final.surface
                }.getOrDefault(stem)
                ExecutionExpression.Pada(surface)
            }.toMutableList<ExecutionExpression>()
        (vakya as? AkhyataVakya)?.tinganta?.dhatu?.mulaDhatu?.let { verb ->
            words += ExecutionExpression.Pada(verb)
        }
        val karman = when (words.size) {
            0 -> null
            1 -> words.single()
            else -> ExecutionExpression.Coordination(words)
        }
        return transferable + listOfNotNull(karman?.let { Karaka.KARMAN to it })
    }

    /** Finds quotation/reporting pairs at any depth without scheduling quoted commands. */
    private fun quotationBindings(root: ProgramNode): Map<Vakya, Vakya> = buildMap {
        root.depthFirst().filterIsInstance<Quotation>().forEach { quotation ->
            val reporting = quotation.reporting as? Invocation ?: return@forEach
            put(reporting.vakya, quotation.quoted.vakya)
        }
    }

    /** Maps a ततः target invocation to the single invocation that immediately precedes it. */
    private fun pipelineKarmanSources(root: ProgramNode): Map<Int, Int> {
        data class Shape(val entries: Set<Int>, val exits: Set<Int>)

        var nextInvocation = 1
        val sources = mutableMapOf<Int, Int>()

        fun visit(node: ProgramNode): Shape = when (node) {
            is Invocation -> {
                val id = nextInvocation++
                Shape(setOf(id), setOf(id))
            }
            is Sequence -> {
                val shapes = node.statements.map(::visit)
                shapes.zipWithNext().forEachIndexed { boundary, (before, after) ->
                    if (node.connectors.getOrNull(boundary) == "ततः" &&
                        before.exits.size == 1 && after.entries.size == 1
                    ) {
                        sources[after.entries.single()] = before.exits.single()
                    }
                }
                Shape(shapes.first().entries, shapes.last().exits)
            }
            is Conditional -> {
                val condition = visit(node.condition)
                val consequent = visit(node.consequent)
                val alternate = node.alternate?.let(::visit)
                Shape(condition.entries, consequent.exits + (alternate?.exits ?: emptySet()))
            }
            is Quotation -> visit(node.reporting)
            is Repeat -> {
                val repetitions = List(node.count) { visit(node.body) }
                Shape(repetitions.first().entries, repetitions.last().exits)
            }
            is WhileLoop -> {
                val body = visit(node.body)
                node.exhausted?.let(::visit)
                node.resultTarget?.let(::visit)
                body
            }
            is Pipeline -> Shape(emptySet(), emptySet())
            is Procedure -> Shape(emptySet(), emptySet())
            is Scope -> Shape(emptySet(), emptySet())
        }

        visit(root)
        return sources
    }

    private fun lowerFrequencyQualifiers(
        root: ProgramNode,
        analysis: UktiAnalysis,
    ): ProgramNode {
        val transformer = object : ProgramNodeTransformer() {
            var frameIndex = 0
            override fun visitInvocation(node: Invocation): ProgramNode {
                val frame = analysis.frames[frameIndex++]
                val count = FrequencyExtractor.extractFrequencyCount(node.vakya.padas, frame)
                return if (count != null && count > 1) Repeat(node.sourceText, count, node) else node
            }
        }
        return transformer.transform(root)
    }
}
