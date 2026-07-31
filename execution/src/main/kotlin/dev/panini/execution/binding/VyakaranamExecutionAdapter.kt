package dev.panini.execution.binding

import dev.panini.core.Karaka
import dev.panini.core.Lakara
import dev.panini.dhatupatha.Dhatu
import dev.panini.execution.DhatuInvocation
import dev.panini.execution.ExecutableUkti
import dev.panini.execution.ExecutionBindingResult
import dev.panini.execution.ExecutionExpression
import dev.panini.execution.GrammaticalFeatures
import dev.panini.execution.Polarity
import dev.panini.execution.SambhashanaContext
import dev.panini.execution.SanskritUktiInput
import dev.panini.execution.VakyaPrayojana
import dev.panini.execution.bindingName
import dev.panini.analysis.PadaAnalyzer
import dev.panini.analysis.UktiAnalyzer
import dev.panini.analysis.VakyaAnalyzer
import dev.panini.vyakaranam.ast.AkhyataVakya
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.SankhyaAbhyasaPada
import dev.panini.vyakaranam.ast.TingantaPada
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

    fun bind(input: SanskritUktiInput, conversation: SambhashanaContext): ExecutionBindingResult {
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
        val utteranceAnalysis = UktiAnalyzer { vakya, frameId ->
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

        val invocations = mutableListOf<DhatuInvocation>()
        var prayer = false
        var prohibition = false
        val localVariables = mutableSetOf<String>()
        val localVariableInvocationIds = mutableMapOf<String, String>()

        // Compute whole-utterance repeat count for multi-clause unrolling.
        // Single-clause repetition loops are evaluated in-memory by the ExecutionRuntime loop.
        val abhyasaCounts = ukti.vakyas.flatMap { vakya ->
            vakya.padas.filterIsInstance<SankhyaAbhyasaPada>().mapNotNull { pada ->
                val numStems = pada.stems.filterNot { it in FrequencyExtractor.ABHYASA_SUFFIX_STEMS }
                val evaluated = if (numStems.isNotEmpty()) {
                    sharedSankhyaEvaluator.evaluateStems(numStems)
                } else {
                    sharedSankhyaEvaluator.evaluateStems(pada.stems)
                }
                evaluated.value.toInt().takeIf { it > 0 }
            }
        }
        val repeatCount = abhyasaCounts.maxOrNull() ?: 1
        val shouldUnroll = repeatCount > 1
        val unrolledVakyas = buildList {
            repeat(if (shouldUnroll) repeatCount else 1) { addAll(ukti.vakyas) }
        }

        unrolledVakyas.forEachIndexed { index, vakya ->
            val padas = vakya.padas
            padas.filterIsInstance<AvyayaPada>().forEach {
                prayer = prayer || it.form == "कृपया"
                prohibition = prohibition || it.form == "मा"
            }
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
                shouldUnroll = shouldUnroll,
            )
            invocations += invocation
            val bindingKaraka = dhatu.operations.firstOrNull { it.resultBindingKaraka != null }?.resultBindingKaraka
            val bindingName = bindingKaraka?.let { invocation.bindings[it] }?.bindingName()
            if (bindingName != null) {
                localVariables.add(bindingName)
                localVariableInvocationIds[bindingName] = "योग-${index + 1}"
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
            ),
            listOf("Bound canonical vyākaraṇa AST with ${ukti.vakyas.size} clause(s) directly to execution."),
        )
    }

    private fun purposeRequiresListenerAsAgent(prayer: Boolean, lakara: Lakara): Boolean =
        prayer || lakara == Lakara.LOT

    /**
     * Extracts kāraka bindings for [padas] and constructs the [DhatuInvocation] for
     * clause number [index] (0-based).
     *
     * @param shouldUnroll When true the utterance is being unrolled across multiple
     *                     repetitions, so per-clause frequency metadata is suppressed
     *                     (the repeat count is encoded at the utterance level instead).
     */
    private fun buildDhatuInvocation(
        index: Int,
        padas: List<Pada>,
        ctx: BindingContext,
        dhatu: Dhatu,
        tinganta: TingantaPada?,
        listener: String,
        prayer: Boolean,
        shouldUnroll: Boolean,
    ): DhatuInvocation {
        val extracted = KarakaExtractor.extractKarakas(padas, ctx)
        val bindings = extracted.bindings.toMutableMap()
        if (tinganta != null && purposeRequiresListenerAsAgent(prayer, tinganta.lakara) && Karaka.KARTR !in bindings) {
            bindings[Karaka.KARTR] = ExecutionExpression.Pada(listener)
        }
        val frequencyCount = if (shouldUnroll) null else FrequencyExtractor.extractFrequencyCount(padas, ctx.frame)
        val metadataMap = buildMap {
            put("dhatuName", dhatu.upadesha)
            put("dhatu:योग-${index + 1}", dhatu.upadesha)
            if (frequencyCount != null) put("frequencyCount", frequencyCount.toString())
        }
        return DhatuInvocation(
            id = "योग-${index + 1}",
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
}
