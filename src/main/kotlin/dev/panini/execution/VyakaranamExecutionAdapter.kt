package dev.panini.execution

import dev.panini.core.Karaka
import dev.panini.core.Lakara
import dev.panini.core.SupAffix
import dev.panini.dhatupatha.Dhatu
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.vyakaranam.ast.*
import dev.panini.vyakaranam.parser.PaniniParseException
import dev.panini.vyakaranam.parser.PaniniParser
import dev.panini.vyakaranam.analysis.KarakaInference
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
            val bindings = extractKarakas(vakya.padas, conversation, index).toMutableMap()
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
    ): Map<Karaka, ExecutionExpression> {
        val grouped = mutableMapOf<Karaka, MutableList<ExecutionExpression>>()
        fun add(subanta: SubantaPada) {
            val affix = SupAffix.fromUpadesha(subanta.sup.text)
            val karaka = affix?.vibhakti?.let { KarakaInference.infer(it, Prayoga.KARTARI) }
                ?: Karaka.ANIRDHARITA
            grouped.getOrPut(karaka) { mutableListOf() } += expression(subanta, conversation, clauseIndex)
        }
        padas.forEach { pada ->
            when (pada) {
                is SubantaPada -> add(pada)
                is SamuccitaSubanta -> {
                    val members = pada.members.map { expression(it, conversation, clauseIndex) }
                    val karaka = pada.members.firstOrNull()?.let {
                        SupAffix.fromUpadesha(it.sup.text)?.vibhakti
                            ?.let { vibhakti -> KarakaInference.infer(vibhakti, Prayoga.KARTARI) }
                            ?: Karaka.ANIRDHARITA
                    } ?: Karaka.KARMAN
                    grouped.getOrPut(karaka) { mutableListOf() } += ExecutionExpression.Coordination(members)
                }
                else -> Unit
            }
        }
        return grouped.mapValues { (_, values) ->
            if (values.size == 1) values.single() else ExecutionExpression.Coordination(values)
        }
    }

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
        val matches = DhatuPatha.all.filter {
            it.upadesha == text || it.derivationalSurface == text || it.id == text
        }
        return matches.firstOrNull { DhatuOperationRegistry.DEFAULT.operationsFor(it).isNotEmpty() }
            ?: DhatuPatha.findByUpadesha(text).firstOrNull {
                DhatuOperationRegistry.DEFAULT.operationsFor(it).isNotEmpty()
            }
            ?: explicitDhatu(text)
    }

    private fun explicitDhatu(text: String): Dhatu? = when {
        text.startsWith("युज्") -> DhatuPatha.find("07.0007")
        text.startsWith("गण") -> DhatuPatha.find("10.0391")
        text.startsWith("हृ") -> DhatuPatha.find("01.0002")
        text.startsWith("कृ") -> DhatuPatha.find("08.0010")
        text.startsWith("भज") -> DhatuPatha.find("01.1153")
        text.startsWith("दा") -> DhatuPatha.find("03.0010")
        text.startsWith("दृश्") -> DhatuPatha.find("01.1143")
        text.startsWith("प्रेष") -> DhatuPatha.find("10.0509")
        else -> null
    }

}
