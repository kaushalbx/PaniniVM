package dev.panini.execution.binding

import dev.panini.core.Karaka
import dev.panini.core.Lakara
import dev.panini.core.Prayoga
import dev.panini.core.SupAffix
import dev.panini.dhatupatha.Dhatu
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.execution.AmbiguousKarakaBinding
import dev.panini.execution.DhatuInvocation
import dev.panini.execution.ExecutableUkti
import dev.panini.execution.ExecutionBindingResult
import dev.panini.execution.ExecutionExpression
import dev.panini.shiksha.Samjna
import dev.panini.execution.GrammaticalFeatures
import dev.panini.execution.Polarity
import dev.panini.execution.SambhashanaContext
import dev.panini.execution.SanskritUktiInput
import dev.panini.execution.VakyaPrayojana
import dev.panini.sankhya.SankhyaGenerator
import dev.panini.shiksha.Karmatva
import dev.panini.analysis.DhatuIdentity
import dev.panini.analysis.DhatuKarakaProfiles
import dev.panini.analysis.KarakaRuleContext
import dev.panini.analysis.KarakaRuleEngine
import dev.panini.analysis.ParticipantFacts
import dev.panini.vyakaranam.ast.AkhyataVakya
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.KridantaPratipadika
import dev.panini.aryabhatiya.AryabhatiyaDecoder
import dev.panini.vyakaranam.ast.AryabhatiyaPada
import dev.panini.bhutasamkhya.BhutasamkhyaDecoder
import dev.panini.vyakaranam.ast.BhutasamkhyaPada
import dev.panini.katapayadi.KatapayadiDecoder
import dev.panini.vyakaranam.ast.KatapayadiPada
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.Pratipadika
import dev.panini.vyakaranam.ast.SamasaPratipadika
import dev.panini.vyakaranam.ast.SamuccitaSubanta
import dev.panini.vyakaranam.ast.SankhyaAbhyasaPada
import dev.panini.vyakaranam.ast.SankhyaPada
import dev.panini.vyakaranam.ast.SankhyaPratipadika
import dev.panini.vyakaranam.ast.SankhyaPuranaPada
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.TingantaPada
import dev.panini.vyakaranam.ast.UnadyantaPratipadika
import dev.panini.vyakaranam.parser.PaniniParseException
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.collections.plusAssign

/**
 * Thin bridge from canonical vyākaraṇa analysis to execution semantics.
 * Grammatical case-to-kāraka policy remains owned by the vyākaraṇa package.
 */
object VyakaranamExecutionAdapter {
    private val parser = PaniniParser()
    private val sankhyaGenerator = SankhyaGenerator()
    private val sankhyaEvaluator = dev.panini.sankhya.SankhyaEvaluator()
    private val katapayadiDecoder = KatapayadiDecoder()
    private val aryabhatiyaDecoder = AryabhatiyaDecoder()
    private val bhutasamkhyaDecoder = BhutasamkhyaDecoder()

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
        val invocations = mutableListOf<DhatuInvocation>()
        var prayer = false
        var prohibition = false

        ukti.vakyas.forEachIndexed { index, vakya ->
            vakya.padas.filterIsInstance<AvyayaPada>().forEach {
                prayer = prayer || it.form == "कृपया"
                prohibition = prohibition || it.form == "मा"
            }
            val tinganta = (vakya as? AkhyataVakya)?.tinganta ?: return@forEachIndexed
            val dhatu = resolveDhatu(tinganta)
                ?: return ExecutionBindingResult.Invalid("Unknown verbal action/dhātu: ${tinganta.sourceText}")
            val extracted = extractKarakas(vakya.padas, conversation, index, dhatu, tinganta)
            val bindings = extracted.bindings.toMutableMap()
            if (purposeRequiresListenerAsAgent(prayer, tinganta.lakara) && Karaka.KARTR !in bindings) {
                bindings[Karaka.KARTR] = ExecutionExpression.Pada(listener)
            }
            invocations += DhatuInvocation(
                id = "योग-${index + 1}",
                dhatu = dhatu,
                bindings = bindings,
                selectedOperation = null,
                metadata = mapOf("dhatuName" to dhatu.upadesha),
                grammaticalFeatures = GrammaticalFeatures(
                    upasargas = tinganta.upasargas.toSet(),
                    sanadi = tinganta.dhatu.sanadiPratyayas.toSet(),
                    avyayas = vakya.padas.filterIsInstance<AvyayaPada>()
                        .mapTo(mutableSetOf()) { it.form },
                    lakara = tinganta.lakara,
                ),
                ambiguousBindings = extracted.ambiguous,
                karakaTrace = extracted.trace,
            )
        }

        val lakara = ukti.vakyas.filterIsInstance<AkhyataVakya>().firstOrNull()?.tinganta?.lakara
        val purpose = when {
            prohibition -> VakyaPrayojana.NISHEDHA
            prayer -> VakyaPrayojana.PRARTHANA
            lakara == Lakara.LOT -> VakyaPrayojana.AJNA
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

    private fun extractKarakas(
        padas: List<Pada>,
        conversation: SambhashanaContext?,
        clauseIndex: Int,
        dhatu: Dhatu,
        tinganta: TingantaPada,
    ): ExtractedBindings {
        val grouped = mutableMapOf<Karaka, MutableList<ExecutionExpression>>()
        val ambiguous = mutableListOf<AmbiguousKarakaBinding>()
        val trace = mutableListOf<String>()
        val requiredKarakas = dhatu.operations
            .flatMapTo(mutableSetOf()) { operation -> operation.signature.requirements.map { it.karaka } }
        fun inferKarakas(pada: SubantaPada): Set<Karaka> {
            val possibleVibhaktis = SupAffix.candidates(pada.sup.text).mapTo(mutableSetOf()) { it.vibhakti }
            val profile = DhatuKarakaProfiles.forSurface(dhatu.sourceSurface)
            val participant = ParticipantFacts(
                id = pada.sourceText,
                expression = pada,
                possibleVibhaktis = possibleVibhaktis,
                semanticRelations = profile?.relations.orEmpty(),
            )
            val resolution = KarakaRuleEngine.resolve(
                KarakaRuleContext(
                    dhatu = DhatuIdentity(dhatu.sourceSurface, dhatu.karmatva != Karmatva.AKARMAKA),
                    participant = participant,
                    allParticipants = listOf(participant),
                    prayoga = Prayoga.KARTARI,
                ),
            )
            trace += resolution.evidence.map { "${it.sutra} ${it.text}: ${it.reason}" }
            resolution.resolved?.let { return setOf(it) }
            val candidates = resolution.candidates
            val requiredCandidates = candidates intersect requiredKarakas
            return requiredCandidates.takeIf { it.size == 1 } ?: candidates
        }
        fun addBinding(expression: ExecutionExpression, candidates: Set<Karaka>) {
            when (candidates.size) {
                0 -> grouped.getOrPut(Karaka.ANIRDHARITA) { mutableListOf() } += expression
                1 -> grouped.getOrPut(candidates.single()) { mutableListOf() } += expression
                else -> ambiguous += AmbiguousKarakaBinding(expression, candidates)
            }
        }
        fun add(subanta: SubantaPada) {
            addBinding(expression(subanta, conversation, clauseIndex), inferKarakas(subanta))
        }
        val mathTargetValues = mutableSetOf<Int>()

        fun extractNumeralValue(pada: Pada): Long? = when (pada) {
            is SankhyaPada -> pada.value ?: sankhyaEvaluator.evaluateStems(pada.stems).value
            is SubantaPada -> (pada.pratipadika as? SankhyaPratipadika)?.value
                ?: dev.panini.sankhya.PrimitiveSankhya.fromAnnotatedPratipadika(pada.pratipadika.sourceText)?.value
            is KatapayadiPada -> pada.value ?: katapayadiDecoder.decode(pada.word)
            is AryabhatiyaPada -> pada.value ?: aryabhatiyaDecoder.decode(pada.word)
            is BhutasamkhyaPada -> pada.value ?: bhutasamkhyaDecoder.decodeTerms(pada.terms)
            else -> null
        }

        padas.forEachIndexed { index, pada ->
            if (index in mathTargetValues) return@forEachIndexed
            when (pada) {
                is SubantaPada -> add(pada)
                is SankhyaPada -> {
                    var targetIdx = -1
                    var nextVal: Long? = null
                    val lastStem = pada.stems.lastOrNull()
                    val isOpStem = lastStem in setOf("गुणित", "हते", "भक्त", "हृत", "कृत") || (pada.stems.size >= 1 && pada.stems[0] in setOf("वर्ग", "घन", "मूल"))
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
                    val fullStems = if (nextVal != null && (pada.stems.size <= 2 || (pada.stems.size == 2 && pada.stems[1] == "कृत"))) {
                        val stemStr = dev.panini.sankhya.PrimitiveSankhya.fromValue(nextVal)?.let { if (it.purvapada.isNotEmpty()) it.purvapada else it.pratipadika } ?: "शत"
                        mathTargetValues.add(targetIdx)
                        if (pada.stems[0] in setOf("वर्ग", "घन", "मूल")) {
                            listOf(pada.stems[0]) + (if (pada.stems.size >= 2 && pada.stems[1] == "कृत") listOf("कृत") else emptyList()) + listOf(stemStr)
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
                        ExecutionExpression.Companion.sankhya(value, pada.sourceText),
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
                    val value = pada.value ?: sankhyaEvaluator.evaluateStems(pada.stems).value
                    addBinding(
                        ExecutionExpression.Companion.sankhya(value, pada.sourceText),
                        setOf(Karaka.ANIRDHARITA, Karaka.KARMAN),
                    )
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
                    val members = pada.members.map { expression(it, conversation, clauseIndex) }
                    val candidates = pada.members.firstOrNull()?.let { inferKarakas(it) }.orEmpty()
                    addBinding(ExecutionExpression.Coordination(members), candidates)
                }
                else -> Unit
            }
        }
        val bindings = grouped.mapValues { (_, values) ->
            if (values.size == 1) values.single() else ExecutionExpression.Coordination(values)
        }
        return ExtractedBindings(bindings, ambiguous, trace.distinct())
    }

    private data class ExtractedBindings(
        val bindings: Map<Karaka, ExecutionExpression>,
        val ambiguous: List<AmbiguousKarakaBinding>,
        val trace: List<String>,
    )

    private fun expression(
        pada: SubantaPada,
        conversation: SambhashanaContext?,
        clauseIndex: Int,
    ): ExecutionExpression {
        val text = pada.pratipadika.baseText()
        var resolvedId: String? = null
        var isOrdinalReference = false

        if (text == "फल" || text == "पूर्वफल") {
            resolvedId = if (clauseIndex > 0) "योग-$clauseIndex" else
                conversation?.resultHistory?.lastOrNull()?.id ?: conversation?.previousResults?.keys?.lastOrNull()
        } else if (text.endsWith("फल")) {
            val prefix = text.removeSuffix("फल")
            val idx = (1..20).firstOrNull { i ->
                sankhyaGenerator.ordinal(i.toLong()).final.surface == prefix ||
                sankhyaGenerator.ordinalVariants(i.toLong()).any { it.final.surface == prefix }
            }?.minus(1)
            if (idx != null) {
                resolvedId = conversation?.resultHistory?.getOrNull(idx)?.id
                isOrdinalReference = true
            }
        }

        if (resolvedId != null) {
            return ExecutionExpression.Reference(resolvedId)
        }

        val sankhyaValue = when (val prat = pada.pratipadika) {
            is SankhyaPratipadika -> prat.value
            is MulaPratipadika -> sankhyaGenerator.annotatedPratipadikaValue(prat.text)
            else -> null
        }
        val samjnas = buildSet {
            add(Samjna.SHABDA)
            if (sankhyaValue != null) add(Samjna.SANKHYA)
            if (text in setOf("फल", "पूर्वफल") || isOrdinalReference) add(Samjna.REFERENCE)
            when (pada.pratipadika) {
                is KridantaPratipadika -> add(Samjna.KRIDANTA)
                is SamasaPratipadika -> add(Samjna.SAMASA)
                else -> Unit
            }
        }
        return if (sankhyaValue != null) {
            ExecutionExpression.Companion.sankhya(sankhyaValue, text)
        } else {
            ExecutionExpression.Pada(text, samjnas)
        }
    }

    private fun Pratipadika.baseText(): String = when (this) {
        is SankhyaPratipadika -> sourceText
        is MulaPratipadika -> text
        is KridantaPratipadika -> dhatu.mulaDhatu
        is UnadyantaPratipadika -> {
            sourceText
        }
        is SamasaPratipadika -> angas.joinToString("-") { it.pratipadika.baseText() }
    }

    private fun resolveDhatu(tinganta: TingantaPada): Dhatu? {
        val text = tinganta.dhatu.mulaDhatu
        val normalizedText = text.normalizeDhatuSurface()
        return DhatuPatha.all.asSequence()
            .filter { it.operations.isNotEmpty() }
            .firstOrNull { dhatu ->
                text == dhatu.id ||
                    text == dhatu.upadesha ||
                    text == dhatu.sourceSurface ||
                    text == dhatu.derivationalSurface ||
                    normalizedText == dhatu.upadesha.normalizeDhatuSurface() ||
                    normalizedText == dhatu.sourceSurface.normalizeDhatuSurface() ||
                    normalizedText == dhatu.derivationalSurface.normalizeDhatuSurface()
            }
    }

    private fun String.normalizeDhatuSurface(): String = trimEnd('्', 'ँ')

}
