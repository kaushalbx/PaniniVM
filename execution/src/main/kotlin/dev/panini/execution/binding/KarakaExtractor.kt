package dev.panini.execution.binding

import dev.panini.aryabhatiya.AryabhatiyaDecoder
import dev.panini.bhutasamkhya.BhutasamkhyaDecoder
import dev.panini.core.Karaka
import dev.panini.core.Prayoga
import dev.panini.core.SupAffix
import dev.panini.execution.AmbiguousKarakaBinding
import dev.panini.execution.ExecutionExpression
import dev.panini.katapayadi.KatapayadiDecoder
import dev.panini.analysis.FrameKarakaResolution
import dev.panini.analysis.KarakaInference
import dev.panini.sankhya.PrimitiveSankhya
import dev.panini.sankhya.SankhyaEvaluator
import dev.panini.shiksha.Karmatva
import dev.panini.vyakaranam.ast.AryabhatiyaPada
import dev.panini.vyakaranam.ast.BhutasamkhyaPada
import dev.panini.vyakaranam.ast.KatapayadiPada
import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.SamuccitaSubanta
import dev.panini.vyakaranam.ast.SankhyaAbhyasaPada
import dev.panini.vyakaranam.ast.SankhyaPada
import dev.panini.vyakaranam.ast.SankhyaPratipadika
import dev.panini.vyakaranam.ast.SankhyaPuranaPada
import dev.panini.vyakaranam.ast.SubantaPada

/**
 * Extracts kāraka bindings from a clause's pādas, delegating:
 * - फल reference resolution to [PhalaResolver]
 * - SubantaPada → ExecutionExpression conversion to [ExpressionBuilder]
 *
 * Grammatical case-to-kāraka policy remains owned by the vyākaraṇa package;
 * this object is only responsible for mapping resolved kārakas to execution expressions.
 */
internal object KarakaExtractor {
    private val sankhyaEvaluator = SankhyaEvaluator()
    private val katapayadiDecoder = KatapayadiDecoder()
    private val aryabhatiyaDecoder = AryabhatiyaDecoder()
    private val bhutasamkhyaDecoder = BhutasamkhyaDecoder()

    /** Result of a single [extractKarakas] call. */
    internal data class ExtractedBindings(
        val bindings: Map<Karaka, ExecutionExpression>,
        val ambiguous: List<AmbiguousKarakaBinding>,
        val trace: List<String>,
    )

    /**
     * Extracts kāraka bindings from [padas] within [ctx].
     *
     * Handles all pada varieties (subanta, sankhya, katapayadi, aryabhatiya,
     * bhutasamkhya, samuccita). फल reference resolution is fully delegated to
     * [PhalaResolver]; expression building is delegated to [ExpressionBuilder].
     */
    internal fun extractKarakas(
        padas: List<Pada>,
        ctx: BindingContext,
    ): ExtractedBindings {
        val grouped = mutableMapOf<Karaka, MutableList<ExecutionExpression>>()
        val ambiguous = mutableListOf<AmbiguousKarakaBinding>()
        val trace = mutableListOf<String>()
        val requiredKarakas = ctx.dhatu.operations
            .flatMapTo(mutableSetOf()) { operation ->
                operation.signature.requirements.map { it.karaka } + operation.signature.optionalKarakas
            }

        val subantas = padas.filterIsInstance<SubantaPada>()
        val phalaPadas = subantas.filter { it.pratipadika.baseText() == "फल" }

        // ---- फल resolution (delegated) ---------------------------------------------
        val phalaResolution = PhalaResolver.resolve(phalaPadas, subantas, ctx)

        // ---- kāraka inference helpers -----------------------------------------------

        fun inferKarakas(pada: SubantaPada): Set<Karaka> {
            val relation = ctx.frame.relations.firstOrNull {
                it.participant.pada.sourceText == pada.sourceText
            }
            if (relation == null) {
                val supAffix = SupAffix.fromUpadesha(pada.sup.text)
                if (supAffix != null) {
                    val effectivePrayoga = if (ctx.frame.prayoga == Prayoga.ANIRDHARITA) {
                        Prayoga.KARTARI
                    } else {
                        ctx.frame.prayoga
                    }
                    val inferred = KarakaInference.infer(
                        supAffix.vibhakti,
                        effectivePrayoga,
                        ctx.dhatu.karmatva != Karmatva.AKARMAKA,
                    )
                    if (inferred != null) return setOf(inferred)
                }
                return emptySet()
            }
            trace += relation.evidence.map { "${it.sutra} ${it.text}: ${it.reason}" }
            val candidates = when (val resolution = relation.resolution) {
                is FrameKarakaResolution.Resolved -> {
                    if (resolution.karaka in requiredKarakas) return setOf(resolution.karaka)
                    setOf(resolution.karaka)
                }
                is FrameKarakaResolution.Ambiguous -> resolution.candidates
                is FrameKarakaResolution.Unassigned -> emptySet()
            }
            val requiredCandidates = candidates intersect requiredKarakas
            if (requiredCandidates.size == 1) return requiredCandidates
            val legacyMorphologicalCandidates = relation.participant.supCandidates.mapNotNullTo(mutableSetOf()) {
                val effectivePrayoga = if (ctx.frame.prayoga == Prayoga.ANIRDHARITA) {
                    Prayoga.KARTARI
                } else {
                    ctx.frame.prayoga
                }
                KarakaInference.infer(
                    it.vibhakti,
                    effectivePrayoga,
                    ctx.dhatu.karmatva != Karmatva.AKARMAKA,
                )
            }
            val compatibleLegacyCandidates = legacyMorphologicalCandidates intersect requiredKarakas
            return compatibleLegacyCandidates.takeIf { it.size == 1 } ?: candidates
        }

        fun addBinding(expression: ExecutionExpression, candidates: Set<Karaka>) {
            when (candidates.size) {
                0 -> grouped.getOrPut(Karaka.ANIRDHARITA) { mutableListOf() } += expression
                1 -> grouped.getOrPut(candidates.single()) { mutableListOf() } += expression
                else -> ambiguous += AmbiguousKarakaBinding(expression, candidates)
            }
        }

        fun add(subanta: SubantaPada, overridePhalaId: String? = null) {
            val phalaId = overridePhalaId ?: phalaResolution.phalaMap[subanta]
            addBinding(
                ExpressionBuilder.build(subanta, ctx, phalaId),
                inferKarakas(subanta),
            )
        }

        val consumedPadaIndices = mutableSetOf<Int>()

        // ---- numeral extraction helper ----------------------------------------------

        fun extractNumeralValue(pada: Pada): Long? = when (pada) {
            is SankhyaPada -> pada.value ?: sankhyaEvaluator.evaluateStems(pada.stems).value
            is SubantaPada -> (pada.pratipadika as? SankhyaPratipadika)?.value
                ?: PrimitiveSankhya.fromAnnotatedPratipadika(pada.pratipadika.sourceText)?.value
            is KatapayadiPada -> pada.value ?: katapayadiDecoder.decode(pada.word)
            is AryabhatiyaPada -> pada.value ?: aryabhatiyaDecoder.decode(pada.word)
            is BhutasamkhyaPada -> pada.value ?: bhutasamkhyaDecoder.decodeTerms(pada.terms)
            else -> null
        }

        // ---- main pada dispatch -----------------------------------------------------

        padas.forEachIndexed { index, pada ->
            if (index in consumedPadaIndices) return@forEachIndexed
            if (pada in phalaResolution.resolvedGenitives) return@forEachIndexed
            when (pada) {
                is SubantaPada -> add(pada, phalaResolution.phalaMap[pada])
                is SankhyaPada -> {
                    if (pada.stems.contains("कृत्वः") || pada.stems.contains("कृत्वस")) return@forEachIndexed
                    var targetIdx = -1
                    var nextVal: Long? = null
                    val lastStem = pada.stems.lastOrNull()
                    val isOpStem = lastStem in setOf("गुणित", "हते", "भक्त", "हृत", "कृत") ||
                        (pada.stems.size >= 1 && pada.stems[0] in setOf("वर्ग", "घन", "मूल"))
                    if (isOpStem) {
                        for (j in (index + 1) until padas.size) {
                            val v = extractNumeralValue(padas[j])
                            if (v != null) {
                                targetIdx = j
                                nextVal = v
                                break
                            }
                        }
                    }
                    val fullStems = if (nextVal != null &&
                        (pada.stems.size <= 2 || (pada.stems.size == 2 && pada.stems[1] == "कृत"))
                    ) {
                        val stemStr = PrimitiveSankhya.fromValue(nextVal)?.let {
                            if (it.purvapada.isNotEmpty()) it.purvapada else it.pratipadika
                        } ?: "शत"
                        consumedPadaIndices.add(targetIdx)
                        if (pada.stems[0] in setOf("वर्ग", "घन", "मूल")) {
                            listOf(pada.stems[0]) +
                                (if (pada.stems.size >= 2 && pada.stems[1] == "कृत") listOf("कृत") else emptyList()) +
                                listOf(stemStr)
                        } else {
                            pada.stems + listOf(stemStr)
                        }
                    } else {
                        pada.stems
                    }
                    val expr = sankhyaEvaluator.evaluateStems(fullStems)
                    val value = expr.value
                    val sub = SubantaPada(pada.sourceText, SankhyaPratipadika(pada.sourceText, value), pada.sup)
                    val candidates = inferKarakas(sub)
                    addBinding(
                        ExecutionExpression.sankhya(value, pada.sourceText),
                        candidates,
                    )
                }
                is SankhyaPuranaPada -> {
                    val value = pada.value ?: sankhyaEvaluator.evaluateStems(pada.stems).value
                    val sub = SubantaPada(pada.sourceText, SankhyaPratipadika(pada.sourceText, value), pada.sup)
                    val candidates = inferKarakas(sub)
                    addBinding(
                        ExecutionExpression.Companion.sankhya(value, pada.sourceText),
                        candidates,
                    )
                }
                is SankhyaAbhyasaPada -> {
                    // अभ्यास-सङ्ख्या qualifies the action with a repetition count; it is
                    // metadata for execution, not one of the action's numeric arguments.
                    Unit
                }
                is KatapayadiPada -> {
                    val value = pada.value ?: katapayadiDecoder.decode(pada.word)
                    val sub = SubantaPada(pada.sourceText, SankhyaPratipadika(pada.sourceText, value), pada.sup)
                    val candidates = inferKarakas(sub)
                    addBinding(
                        ExecutionExpression.Companion.sankhya(value, pada.sourceText),
                        candidates,
                    )
                }
                is AryabhatiyaPada -> {
                    val value = pada.value ?: aryabhatiyaDecoder.decode(pada.word)
                    val sub = SubantaPada(pada.sourceText, SankhyaPratipadika(pada.sourceText, value), pada.sup)
                    val candidates = inferKarakas(sub)
                    addBinding(
                        ExecutionExpression.Companion.sankhya(value, pada.sourceText),
                        candidates,
                    )
                }
                is BhutasamkhyaPada -> {
                    val value = pada.value ?: bhutasamkhyaDecoder.decodeTerms(pada.terms)
                    val sub = SubantaPada(pada.sourceText, SankhyaPratipadika(pada.sourceText, value), pada.sup)
                    val candidates = inferKarakas(sub)
                    addBinding(
                        ExecutionExpression.Companion.sankhya(value, pada.sourceText),
                        candidates,
                    )
                }
                is SamuccitaSubanta -> {
                    val members = pada.members.map { ExpressionBuilder.build(it, ctx) }
                    val allCandidates = pada.members.flatMapTo(mutableSetOf()) { inferKarakas(it) }
                    val candidates = (allCandidates intersect requiredKarakas).ifEmpty { allCandidates }
                    addBinding(ExecutionExpression.Coordination(members), candidates)
                }
                else -> Unit
            }
        }

        // ---- post-processing: collapse multiple bindings for the same kāraka --------

        val bindings = grouped.mapValues { (karaka, values) ->
            val filteredValues = if (karaka == Karaka.KARMAN && values.size > 1) {
                val abhyasaPadas = padas.filterIsInstance<SankhyaAbhyasaPada>()
                val nonAbhyasa = values.filterNot { expr ->
                    expr is ExecutionExpression.Pada && abhyasaPadas.any { p ->
                        p.sourceText.contains(expr.prakriti) ||
                            expr.prakriti.contains(p.sourceText) ||
                            p.stems.contains(expr.prakriti)
                    }
                }
                if (nonAbhyasa.isNotEmpty()) nonAbhyasa else values
            } else {
                values
            }
            if (filteredValues.size == 1) filteredValues.single()
            else ExecutionExpression.Coordination(filteredValues)
        }
        trace += ctx.frame.qualifications.map { "Kriyā qualification ${it.kind}: ${it.value}" }
        return ExtractedBindings(bindings, ambiguous, trace.distinct())
    }
}
