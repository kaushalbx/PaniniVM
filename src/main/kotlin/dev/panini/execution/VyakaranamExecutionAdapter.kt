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

    fun analyze(input: SanskritUktiInput, conversation: SambhashanaContext? = null): ExecutionAnalysisResult {
        if (input.text.isBlank()) return ExecutionAnalysisResult.Unsupported("The Sanskrit utterance is empty.")
        val ukti = try {
            parser.parse(input.text)
        } catch (e: PaniniParseException) {
            return ExecutionAnalysisResult.Unsupported(e.message ?: "Invalid annotated Sanskrit morphology.")
        }

        var listener = input.listener
        ukti.sambodhana?.subanta?.pratipadika?.baseText()?.let { addressed ->
            if (!input.listener.startsWith(addressed)) listener = addressed
        }

        val kriyas = mutableListOf<ExecutionKriyaAnalysis>()
        var prayer = false
        var prohibition = false

        ukti.vakyas.forEachIndexed { index, vakya ->
            vakya.padas.filterIsInstance<AvyayaPada>().forEach {
                prayer = prayer || it.form == "कृपया"
                prohibition = prohibition || it.form == "मा"
            }
            val tinganta = (vakya as? AkhyataVakya)?.tinganta ?: return@forEachIndexed
            val dhatu = resolveDhatu(tinganta)
                ?: return ExecutionAnalysisResult.Unsupported("Unknown verbal action/dhātu: ${tinganta.sourceText}")
            val bindings = extractKarakas(vakya.padas, conversation, index)
            val operation = selectOperation(dhatu, tinganta, vakya.padas)
            kriyas += ExecutionKriyaAnalysis(
                id = "योग-${index + 1}",
                dhatuId = dhatu.id,
                karakas = bindings,
                selectedOperation = operation,
                metadata = mapOf("dhatuName" to dhatu.upadesha),
            )
        }

        val lakara = ukti.vakyas.filterIsInstance<AkhyataVakya>().firstOrNull()?.tinganta?.lakara
        val purpose = when {
            prohibition -> VakyaPrayojana.NISHEDHA
            prayer -> VakyaPrayojana.PRARTHANA
            lakara == Lakara.LOT -> VakyaPrayojana.AJNA
            else -> VakyaPrayojana.VIDHANA
        }
        return ExecutionAnalysisResult.Analyzed(
            ExecutionUtteranceAnalysis(
                speaker = input.speaker,
                listener = listener,
                sourceText = input.text,
                prayojana = purpose,
                polarity = if (prohibition) Polarity.NEGATIVE else Polarity.POSITIVE,
                lakara = lakara,
                kriyas = kriyas,
            ),
            listOf("Analyzed canonical vyākaraṇa AST with ${ukti.vakyas.size} clause(s)."),
        )
    }

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
        return matches.firstOrNull { it.operations.isNotEmpty() }
            ?: DhatuPatha.findByUpadesha(text).firstOrNull { it.operations.isNotEmpty() }
            ?: explicitDhatu(text)
            ?: matches.firstOrNull()
            ?: DhatuPatha.findByUpadesha(text).firstOrNull()
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

    private fun selectOperation(dhatu: Dhatu, tinganta: TingantaPada, padas: List<Pada>): String? {
        if (dhatu.operations.size <= 1) return dhatu.operations.firstOrNull()?.id
        val text = tinganta.dhatu.mulaDhatu
        return when (dhatu.id) {
            "07.0007" -> if (tinganta.upasargas.contains("वि")) "सङ्ख्यावियोगः" else "सङ्ख्यायोजनम्"
            "10.0391" -> if (text.startsWith("सम")) "सङ्ख्यासाम्यम्" else "सङ्ख्यागुणनम्"
            "08.0010" -> if (padas.filterIsInstance<AvyayaPada>().any { it.form == "इति" }) "संहिताकरणम्" else "पदनिष्पत्तिः"
            "01.0002" -> "सङ्ख्याहरणम्"
            "01.1153" -> "सङ्ख्याभागः"
            "03.0010" -> "मूल्यदानम्"
            "01.1143" -> "मूल्यदर्शनम्"
            else -> dhatu.operations.first().id
        }
    }

}
