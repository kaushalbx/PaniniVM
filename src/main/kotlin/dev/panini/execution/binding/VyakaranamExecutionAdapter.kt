package dev.panini.execution

import dev.panini.core.Karaka
import dev.panini.core.Lakara
import dev.panini.dhatupatha.Dhatu
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.vyakaranam.ast.*
import dev.panini.vyakaranam.parser.PaniniParseException
import dev.panini.vyakaranam.parser.PaniniParser
import dev.panini.vyakaranam.analysis.KarakaRuleContext
import dev.panini.vyakaranam.analysis.KarakaRuleEngine
import dev.panini.core.Prayoga
import dev.panini.sankhya.SankhyaGenerator

/**
 * Thin bridge from canonical vyākaraṇa analysis to execution semantics.
 * Grammatical case-to-kāraka policy remains owned by the vyākaraṇa package.
 */
object VyakaranamExecutionAdapter {
    private val parser = PaniniParser()
    private val sankhyaGenerator = SankhyaGenerator()

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
                    avyayas = vakya.padas.filterIsInstance<AvyayaPada>().mapTo(mutableSetOf()) { it.form },
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
            Ukti(
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
        val requiredKarakas = DhatuOperationRegistry.DEFAULT.operationsFor(dhatu)
            .flatMapTo(mutableSetOf()) { operation -> operation.signature.requirements.map { it.karaka } }
        fun inferKarakas(sup: String): Set<Karaka> {
            val resolution = KarakaRuleEngine.resolve(
                KarakaRuleContext(tinganta.dhatu.mulaDhatu, Prayoga.KARTARI, sup),
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
            addBinding(expression(subanta, conversation, clauseIndex), inferKarakas(subanta.sup.text))
        }
        padas.forEach { pada ->
            when (pada) {
                is SubantaPada -> add(pada)
                is SamuccitaSubanta -> {
                    val members = pada.members.map { expression(it, conversation, clauseIndex) }
                    val candidates = pada.members.firstOrNull()?.let { inferKarakas(it.sup.text) }.orEmpty()
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
        if (text == "फल" || text == "पूर्वफल") {
            val id = if (clauseIndex > 0 && text == "फल") "योग-$clauseIndex" else
                conversation?.resultHistory?.lastOrNull()?.id ?: conversation?.previousResults?.keys?.lastOrNull()
            if (id != null) return ExecutionExpression.Reference(id)
        }
        val sankhyaValue = (pada.pratipadika as? MulaPratipadika)?.let {
            sankhyaGenerator.annotatedPratipadikaValue(it.text)
        }
        val samjnas = buildSet {
            add(ExecutionSamjna.SHABDA)
            if (sankhyaValue != null) add(ExecutionSamjna.SANKHYA)
            if (text in setOf("फल", "पूर्वफल")) add(ExecutionSamjna.REFERENCE)
            when (pada.pratipadika) {
                is KridantaPratipadika -> add(ExecutionSamjna.KRIDANTA)
                is SamasaPratipadika -> add(ExecutionSamjna.SAMASA)
                else -> Unit
            }
        }
        return if (sankhyaValue != null) {
            ExecutionExpression.sankhya(sankhyaValue.longValueExact(), text)
        } else {
            ExecutionExpression.Pada(text, samjnas)
        }
    }

    private fun Pratipadika.baseText(): String = when (this) {
        is MulaPratipadika -> text
        is KridantaPratipadika -> dhatu.mulaDhatu
        is UnadyantaPratipadika -> sourceText
        is SamasaPratipadika -> angas.joinToString("-") { it.pratipadika.baseText() }
    }

    private fun resolveDhatu(tinganta: TingantaPada): Dhatu? {
        val text = tinganta.dhatu.mulaDhatu
        val normalizedText = text.normalizeDhatuSurface()
        return DhatuPatha.all.asSequence()
            .filter(DhatuOperationRegistry.DEFAULT::isExecutable)
            .firstOrNull { dhatu ->
                text == dhatu.id ||
                    text == dhatu.upadesha ||
                    text == dhatu.derivationalSurface ||
                    normalizedText == dhatu.upadesha.normalizeDhatuSurface() ||
                    normalizedText == dhatu.derivationalSurface.normalizeDhatuSurface()
            }
    }

    private fun String.normalizeDhatuSurface(): String = trimEnd('्', 'ँ')

}
