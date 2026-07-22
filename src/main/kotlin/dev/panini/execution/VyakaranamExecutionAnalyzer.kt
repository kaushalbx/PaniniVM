package dev.panini.execution

import dev.panini.core.Karaka
import dev.panini.core.Lakara
import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.dhatupatha.Dhatu
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.vyakaranam.ast.*
import dev.panini.vyakaranam.parser.PaniniParseException
import dev.panini.vyakaranam.parser.PaniniParser

/** Converts the canonical annotated-vyākaraṇa AST into execution semantics. */
object VyakaranamExecutionAnalyzer {
    private val parser = PaniniParser()

    fun analyze(input: SanskritUktiInput, conversation: SambhashanaContext? = null): VakyaAnalysisResult {
        if (input.text.isBlank()) return VakyaAnalysisResult.Unsupported("The Sanskrit utterance is empty.")
        val ukti = try {
            parser.parse(input.text)
        } catch (e: PaniniParseException) {
            return VakyaAnalysisResult.Unsupported(e.message ?: "Invalid annotated Sanskrit morphology.")
        }

        var listener = input.listener
        ukti.sambodhana?.subanta?.pratipadika?.baseText()?.let { addressed ->
            if (!input.listener.startsWith(addressed)) listener = addressed
        }

        val kriyas = mutableListOf<KriyaAnalysis>()
        var prayer = false
        var prohibition = false

        ukti.vakyas.forEachIndexed { index, vakya ->
            vakya.padas.filterIsInstance<AvyayaPada>().forEach {
                prayer = prayer || it.form == "कृपया"
                prohibition = prohibition || it.form == "मा"
            }
            val tinganta = (vakya as? AkhyataVakya)?.tinganta ?: return@forEachIndexed
            val dhatu = resolveDhatu(tinganta)
                ?: return VakyaAnalysisResult.Unsupported("Unknown verbal action/dhātu: ${tinganta.sourceText}")
            val bindings = extractKarakas(vakya.padas, conversation, index)
            val operation = selectOperation(dhatu, tinganta, vakya.padas)
            missingRequirement(dhatu, operation, bindings)?.let {
                return VakyaAnalysisResult.NeedsClarification(it)
            }
            kriyas += KriyaAnalysis(
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
        return VakyaAnalysisResult.Analyzed(
            VakyaAnalysis(
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
            val karaka = when (affix?.vibhakti) {
                Vibhakti.PRATHAMA -> Karaka.KARTR
                Vibhakti.DVITIYA -> Karaka.KARMAN
                Vibhakti.TRTIYA -> Karaka.KARANA
                Vibhakti.CHATURTHI -> Karaka.SAMPRADANA
                Vibhakti.PANCHAMI -> Karaka.APADANA
                Vibhakti.SASTHI -> Karaka.SAMBANDHA
                Vibhakti.SAPTAMI -> Karaka.ADHIKARANA
                null -> Karaka.ANIRDHARITA
            }
            grouped.getOrPut(karaka) { mutableListOf() } += expression(subanta, conversation, clauseIndex)
        }
        padas.forEach { pada ->
            when (pada) {
                is SubantaPada -> add(pada)
                is SamuccitaSubanta -> {
                    val members = pada.members.map { expression(it, conversation, clauseIndex) }
                    val karaka = pada.members.firstOrNull()?.let {
                        when (SupAffix.fromUpadesha(it.sup.text)?.vibhakti) {
                            Vibhakti.PRATHAMA -> Karaka.KARTR
                            Vibhakti.TRTIYA -> Karaka.KARANA
                            Vibhakti.CHATURTHI -> Karaka.SAMPRADANA
                            Vibhakti.PANCHAMI -> Karaka.APADANA
                            Vibhakti.SASTHI -> Karaka.SAMBANDHA
                            Vibhakti.SAPTAMI -> Karaka.ADHIKARANA
                            else -> Karaka.KARMAN
                        }
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
        val samjnas = buildSet {
            add(ExecutionSamjna.SHABDA)
            if (SanskritNumbers.valueOf(text) != null) add(ExecutionSamjna.SANKHYA)
            if (text in setOf("फल", "पूर्वफल")) add(ExecutionSamjna.REFERENCE)
            when (pada.pratipadika) {
                is KridantaPratipadika -> add(ExecutionSamjna.KRIDANTA)
                is SamasaPratipadika -> add(ExecutionSamjna.SAMASA)
                else -> Unit
            }
        }
        return ExecutionExpression.Pada(text, samjnas)
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

    private fun missingRequirement(
        dhatu: Dhatu,
        operationId: String?,
        bindings: Map<Karaka, ExecutionExpression>,
    ): String? {
        val operation = dhatu.operations.find { it.id == operationId } ?: return null
        operation.signature.requirements.forEach { requirement ->
            val expression = bindings[requirement.karaka]
                ?: return "Required kārakas are missing for dhātu ${dhatu.upadesha}: setOf(${requirement.karaka})"
            val count = (expression as? ExecutionExpression.Coordination)?.members?.size ?: 1
            if (count < requirement.minimumMembers) {
                return "Required kārakas are missing for dhātu ${dhatu.upadesha}: setOf(${requirement.karaka})"
            }
        }
        return null
    }
}
