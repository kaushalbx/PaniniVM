package dev.panini.execution.binding

import dev.panini.core.Karaka
import dev.panini.core.Prayoga
import dev.panini.core.Vibhakti
import dev.panini.core.Vacana
import dev.panini.core.Upasarga
import dev.panini.execution.AmbiguousKarakaBinding
import dev.panini.execution.ExecutionExpression
import dev.panini.analysis.FrameKarakaResolution
import dev.panini.analysis.KarakaInference
import dev.panini.sankhya.PrimitiveSankhya
import dev.panini.sankhya.SankhyaOperationMarkers
import dev.panini.shiksha.Karmatva
import dev.panini.vyakaranam.ast.AryabhatiyaPada
import dev.panini.vyakaranam.ast.BhutasamkhyaPada
import dev.panini.vyakaranam.ast.KatapayadiPada
import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.ParyantaRangePada
import dev.panini.vyakaranam.ast.SamuccitaSubanta
import dev.panini.vyakaranam.ast.SankhyaAbhyasaPada
import dev.panini.vyakaranam.ast.SankhyaPada
import dev.panini.vyakaranam.ast.SankhyaPuranaPada
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.MulaPratipadikaIdentity
import dev.panini.vyakaranam.ast.TingantaPada
import dev.panini.vyakaranam.ast.isRecognizedStriAffix

/**
 * Extracts kāraka bindings from a clause's pādas, delegating:
 * - फल reference resolution to [PhalaResolver]
 * - SubantaPada → ExecutionExpression conversion to [ExpressionBuilder]
 * - Numeric pada decoding and binding to [NumeralPadaBinder]
 *
 * Grammatical case-to-kāraka policy remains owned by the vyākaraṇa package;
 * this object is only responsible for mapping resolved kārakas to execution expressions.
 */
internal object KarakaExtractor {

    /**
     * Extracts kāraka bindings from [padas] within [ctx].
     *
     * Handles all pada varieties (subanta, sankhya, katapayadi, aryabhatiya,
     * bhutasamkhya, samuccita). फल reference resolution is fully delegated to
     * [PhalaResolver]; expression building is delegated to [ExpressionBuilder];
     * numeric pada decoding is delegated to [NumeralPadaBinder].
     */
    internal fun extractKarakas(
        sourcePadas: List<Pada>,
        ctx: BindingContext,
    ): ExtractedBindings {
        // Coordination does not hide its members from modifier/history analysis.
        // Keep source order and original identities for the frame's relations.
        val padas = sourcePadas.flatMap { if (it is SamuccitaSubanta) it.members else listOf(it) }
        val grouped = mutableMapOf<Karaka, MutableList<ExecutionExpression>>()
        val ambiguous = mutableListOf<AmbiguousKarakaBinding>()
        val trace = mutableListOf<String>()
        val historyParticipants = mutableListOf<dev.panini.execution.HistoryParticipant>()
        val nominalBindings = mutableMapOf<dev.panini.execution.NominalOperandRole, ExecutionExpression>()
        var parsedCopularPredicate: dev.panini.execution.CopularPredicate? = null
        val subantas = padas.filterIsInstance<SubantaPada>()
        val isCopularClause = padas.filterIsInstance<TingantaPada>().any {
            it.canonicalDhatuIdentity() == CanonicalDhatuIdentity.AS
        }
        val phalaPadas = subantas.filter(PhalaReference::isReference)

        // ---- फल resolution (delegated) ---------------------------------------------
        val phalaResolution = PhalaResolver.resolve(phalaPadas, padas, subantas, ctx)
        val karakaReferenceResolution = KarakaReferenceResolver.resolve(padas, subantas, ctx)

        // ---- kāraka inference helpers -----------------------------------------------

        fun inferKarakas(pada: dev.panini.vyakaranam.ast.SupBearingPada): Set<Karaka> {
            val relation = ctx.relationFor(pada)
            if (relation == null) {
                return emptySet()
            }
            relation.primaryOptionalDecision?.takeIf { !it.applied }?.let {
                trace += "Optional designation ${it.sutra} declined; the underlying canonical designation is retained."
            }
            trace += relation.evidence.map { "${it.sutra} ${it.text}: ${it.reason}" }
            if (relation.coexistingKarakas.size > 1 && relation.resolvedVibhakti == null) return emptySet()
            // A diagnosed semantic dependency cannot be repaired by choosing
            // an operation slot from the same written ending.
            if (relation.unresolvedObjectMediators.isNotEmpty()) return emptySet()
            return when (val resolution = relation.resolution) {
                is FrameKarakaResolution.Resolved -> {
                    // Operation compatibility cannot overturn an established grammatical relation.
                    setOf(resolution.karaka)
                }
                is FrameKarakaResolution.Ambiguous -> resolution.candidates
                is FrameKarakaResolution.Unassigned -> emptySet()
            }
        }

        fun addBinding(expression: ExecutionExpression, candidates: Set<Karaka>,
            sup: dev.panini.vyakaranam.ast.SupPratyaya? = null,
            source: dev.panini.vyakaranam.ast.SupBearingPada? = null) {
            if (candidates.isNotEmpty() && candidates != setOf(Karaka.ANIRDHARITA)) {
                val names = ctx.relationFor(source)?.coexistingKarakas.orEmpty()
                historyParticipants += dev.panini.execution.HistoryParticipant(expression, candidates.singleOrNull(), names,
                    ambiguousIndex = ambiguous.size.takeIf { candidates.size > 1 })
            }
            when (candidates.size) {
                0 -> grouped.getOrPut(Karaka.ANIRDHARITA) { mutableListOf() } += expression
                1 -> grouped.getOrPut(candidates.single()) { mutableListOf() } += expression
                else -> {
                    val prayoga = (ctx.ownerOf(source) ?: ctx.frame).prayoga.takeUnless { it == Prayoga.ANIRDHARITA } ?: Prayoga.KARTARI
                    val slots = candidates.mapNotNull { karaka ->
                        sup?.affixCandidates?.singleOrNull {
                            KarakaInference.infer(it.vibhakti, prayoga, ctx.dhatu.karmatva != Karmatva.AKARMAKA) == karaka
                        }?.let { karaka to it }
                    }.toMap()
                    ambiguous += AmbiguousKarakaBinding(expression, candidates, slots)
                }
            }
        }

        fun numericBinding(pada: dev.panini.vyakaranam.ast.SupBearingPada): (ExecutionExpression, Set<Karaka>) -> Unit =
            { expression, candidates -> addBinding(expression, candidates, pada.sup, pada) }

        fun add(subanta: SubantaPada, overridePhalaId: String? = null) {
            val phalaId = overridePhalaId ?: phalaResolution.phalaMap[subanta]
            val candidates = inferKarakas(subanta)
            val collectionMemberFrame = candidates == setOf(Karaka.KARMAN) &&
                ctx.dhatu.operations.any { it.name in setOf("सङ्ख्यायोजनम्", "सूच्युद्धरणम्") } &&
                subantas.any {
                    it.sup.supports(Vibhakti.SASTHI, Vacana.EKAVACANA) &&
                        it !in phalaResolution.resolvedGenitives &&
                        it !in karakaReferenceResolution.consumedGenitives
                }
            addBinding(
                ExpressionBuilder.build(subanta, ctx, phalaId, collectionMemberFrame,
                    resolvedKaraka = candidates.singleOrNull()),
                candidates,
                subanta.sup,
                subanta,
            )
        }

        val consumedPadaIndices = mutableSetOf<Int>()

        // ---- main pada dispatch -----------------------------------------------------

        padas.forEachIndexed { index, pada ->
            if (index in consumedPadaIndices) return@forEachIndexed
            if (pada in phalaResolution.resolvedGenitives) return@forEachIndexed
            if (pada in phalaResolution.resolvedQualifiers) return@forEachIndexed
            if (pada in karakaReferenceResolution.consumedGenitives) return@forEachIndexed
            if (pada in karakaReferenceResolution.consumedQualifiers) return@forEachIndexed
            val owner = ctx.ownerOf(pada) ?: ctx.frame
            val nominalIndex = owner.nominalRelations?.nominalPadas?.indexOfFirst { it.pada === pada } ?: -1
            val comparison = owner.nominalCaseLinks?.established?.singleOrNull {
                it.dependent == nominalIndex && it.sutra in setOf("2.3.72", "2.3.42")
            }
            val relation = ctx.relationFor(pada)
            if (comparison != null && relation?.evidence?.lastOrNull()?.sutra == comparison.sutra &&
                relation.resolvedVibhakti != null) {
                val expression = if (pada is SubantaPada) ExpressionBuilder.build(pada, ctx,
                    overridePhalaId = phalaResolution.phalaMap[pada],
                    resolvedVibhakti = relation.resolvedVibhakti) else {
                    val value = relation.participant.semanticValue ?: NumeralPadaBinder.resolveSemanticValue(pada)
                        ?: error("The comparison standard has no supported semantic value.")
                    ExecutionExpression.Pada(pada.sourceText, value.samjnas, value)
                }
                require(dev.panini.execution.NominalOperandRole.COMPARISON_STANDARD !in nominalBindings) {
                    "The comparison standard is ambiguous."
                }
                nominalBindings[dev.panini.execution.NominalOperandRole.COMPARISON_STANDARD] = expression
                trace += relation.evidence.map { "${it.sutra} ${it.text}: ${it.reason}" }
                return@forEachIndexed
            }
            if (ctx.dhatu.canonicalDhatuIdentity() == CanonicalDhatuIdentity.HR &&
                padas.filterIsInstance<TingantaPada>().any { Upasarga.UD in it.upasargaIdentities } &&
                subantas.any { it.sup.supports(Vibhakti.SASTHI, Vacana.EKAVACANA) } &&
                (pada !is SubantaPada || !pada.sup.supports(Vibhakti.SASTHI, Vacana.EKAVACANA)) &&
                dev.panini.execution.PuranaPratyayaResolver.ordinalValue(pada) != null) {
                addBinding(requireNotNull(dev.panini.execution.CollectionMemberMorphology.ordinalExpression(pada)),
                    setOf(Karaka.KARMAN))
                return@forEachIndexed
            }
            when (pada) {
                is ParyantaRangePada -> {
                    fun boundaryExpression(
                        boundary: dev.panini.vyakaranam.ast.SankhyaBoundaryPada,
                    ): ExecutionExpression {
                        val value = dev.panini.execution.NaturalSemanticNormalizer.boundaryValue(boundary)
                        if (value != null) return ExecutionExpression.sankhya(value, boundary.sourceText)
                        val reference = boundary.stems.singleOrNull()
                        require(!reference.isNullOrBlank()) {
                            "क्रमसीमायाः पूरणार्थः न ज्ञातः: ${boundary.sourceText}"
                        }
                        return ExecutionExpression.Reference(reference)
                    }
                    val minimum = boundaryExpression(pada.lowerLimit)
                    val maximum = boundaryExpression(pada.upperLimit)
                    addBinding(
                        minimum,
                        setOf(Karaka.APADANA),
                    )
                    addBinding(
                        maximum,
                        setOf(Karaka.ADHIKARANA),
                    )
                    trace += "परि + अन्त + अम् licenses inclusive collection limits."
                }
                is SubantaPada -> {
                    val copularPredicate =
                        (ctx.dhatu.canonicalDhatuIdentity() == CanonicalDhatuIdentity.AS || isCopularClause) &&
                        pada.sup.supports(Vibhakti.PRATHAMA) &&
                        (pada.pratipadika as? MulaPratipadika)?.lexicalIdentity in setOf(
                            MulaPratipadikaIdentity.SAMA,
                            MulaPratipadikaIdentity.NYUNA,
                            MulaPratipadikaIdentity.ADHIKA,
                        )
                    if (copularPredicate) {
                        val nominal = pada.pratipadika as MulaPratipadika
                        require(nominal.vikaras.all { it.isRecognizedStriAffix }) {
                            "A derived nominal cannot silently act as a comparison predicate."
                        }
                        require(parsedCopularPredicate == null) { "Ambiguous copular predicate." }
                        parsedCopularPredicate = requireNotNull(dev.panini.execution.CopularPredicate.from(
                            ExecutionExpression.Pada(nominal.text))) { "Unknown comparison predicate." }
                        return@forEachIndexed
                    }
                    val rememberedParticipant = karakaReferenceResolution.expressions[pada]
                    if (rememberedParticipant != null) {
                        addBinding(rememberedParticipant, inferKarakas(pada))
                    } else {
                        add(pada, phalaResolution.phalaMap[pada])
                    }
                }
                is SankhyaPada -> {
                    // अभ्यास-कृत्वः forms are frequency metadata, not argument values.
                    if (FrequencyExtractor.isAbhyasa(pada.stems)) return@forEachIndexed
                    if (dev.panini.execution.PuranaPratyayaResolver.ordinalValue(pada) != null) {
                        NumeralPadaBinder.bindOrdinal(pada, ::inferKarakas, numericBinding(pada))
                        return@forEachIndexed
                    }
                    // Op-stems (गुणित, वर्ग, …) consume the following numeral pada as their operand.
                    var targetIdx = -1
                    var nextVal: Long? = null
                    if (SankhyaOperationMarkers.needsFollowingOperand(pada.stems)) {
                        for (j in (index + 1) until padas.size) {
                            val v = NumeralPadaBinder.extractNumeralValue(padas[j])
                            if (v != null) { targetIdx = j; nextVal = v; break }
                        }
                    }
                    val fullStems = if (
                        nextVal != null && SankhyaOperationMarkers.acceptsFollowingOperand(pada.stems)
                    ) {
                        val stemStr = PrimitiveSankhya.fromValue(nextVal)?.let {
                            if (it.purvapada.isNotEmpty()) it.purvapada else it.pratipadika
                        } ?: "शत"
                        consumedPadaIndices.add(targetIdx)
                        if (SankhyaOperationMarkers.isBindingPrefix(pada.stems[0])) {
                            listOf(pada.stems[0]) +
                                (
                                    if (pada.stems.size >= 2 &&
                                        SankhyaOperationMarkers.isConstruction(pada.stems[1])
                                    ) {
                                        listOf(pada.stems[1])
                                    } else {
                                        emptyList()
                                    }
                                ) +
                                listOf(stemStr)
                        } else {
                            pada.stems + listOf(stemStr)
                        }
                    } else {
                        pada.stems
                    }
                    val value = NumeralPadaBinder.evaluateStems(fullStems).value
                    NumeralPadaBinder.bindDecoded(pada, value, ::inferKarakas, numericBinding(pada))
                }
                is SankhyaAbhyasaPada -> {
                    // अभ्यास-सङ्ख्या qualifies the action with a repetition count; it is
                    // metadata for execution, not one of the action's numeric arguments.
                }
                is SankhyaPuranaPada -> NumeralPadaBinder.bind(pada, ::inferKarakas, numericBinding(pada))
                is KatapayadiPada    -> NumeralPadaBinder.bind(pada, ::inferKarakas, numericBinding(pada))
                is AryabhatiyaPada   -> NumeralPadaBinder.bind(pada, ::inferKarakas, numericBinding(pada))
                is BhutasamkhyaPada  -> NumeralPadaBinder.bind(pada, ::inferKarakas, numericBinding(pada))
                else -> Unit
            }
        }

        // ---- post-processing: collapse multiple bindings for the same kāraka --------

        val bindings = grouped.mapValues { (_, values) ->
            // SankhyaAbhyasaPada is excluded structurally in the dispatch above;
            // no spelling comparison is needed to distinguish frequency from karman.
            if (values.size == 1) values.single()
            else ExecutionExpression.Coordination(values)
        }
        trace += ctx.grammaticalFrames.flatMap { it.qualifications }.map { "Kriyā qualification ${it.kind}: ${it.value}" }
        return ExtractedBindings(bindings, ambiguous, trace.distinct(), nominalBindings, parsedCopularPredicate,
            historyParticipants.takeIf { records -> records.any { it.coexistingKarakas.isNotEmpty() } }.orEmpty())
    }
}
