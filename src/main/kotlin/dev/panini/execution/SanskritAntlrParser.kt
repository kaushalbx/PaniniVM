package dev.panini.execution

import dev.panini.core.Karaka
import dev.panini.core.Lakara
import dev.panini.core.Prayoga
import dev.panini.dhatupatha.Dhatu
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.parser.VakyaCompiler
import dev.panini.parser.VakyaSyntaxException
import dev.panini.parser.ast.ParsedNominalBase
import dev.panini.parser.ast.ParsedPada
import dev.panini.parser.ast.ParsedSubanta
import dev.panini.parser.ast.ParsedTinganta
import dev.panini.parser.ast.ParsedUtterance
import dev.panini.parser.ast.SimpleNominalKind


/**
 * End-to-end ANTLR4 parser bridge for Sanskrit utterances.
 * Converts segmented or unsegmented source text into structured [VakyaAnalysisResult] ready for execution.
 */
object SanskritAntlrParser {

    fun parse(
        input: SanskritUktiInput,
        conversation: SambhashanaContext? = null,
    ): VakyaAnalysisResult {
        if (input.text.isBlank()) {
            return VakyaAnalysisResult.Unsupported("The Sanskrit utterance is empty.")
        }

        val parsedUtterance: ParsedUtterance = try {
            VakyaCompiler().compile(input.text)
        } catch (exception: VakyaSyntaxException) {
            return VakyaAnalysisResult.Unsupported(
                exception.message ?: "Invalid annotated Sanskrit morphology.",
            )
        }

        return analyzeUtterance(parsedUtterance, input, conversation)
    }

    private fun analyzeUtterance(
        utterance: ParsedUtterance,
        input: SanskritUktiInput,
        conversation: SambhashanaContext?,
    ): VakyaAnalysisResult {
        val trace = mutableListOf<String>()
        trace += "Parsed AST with ${utterance.statements.size} clause(s)."

        var listener = input.listener
        val speaker = input.speaker

        utterance.sambodhana?.let { samb ->
            val addressed = samb.pada.base.text
            if (addressed.isNotBlank()) {
                val inputStem = SanskritMorphologicalParser.parseToken(input.listener).stem
                val addressedStem = SanskritMorphologicalParser.parseToken(addressed).stem
                listener = if (inputStem == addressedStem || input.listener.startsWith(addressedStem)) {
                    input.listener
                } else {
                    addressed
                }
                trace += "Addressed listener from sambodhana: $listener"
            }
        }

        var hasPrarthana = false
        var hasNishedha = false
        val kriyas = mutableListOf<KriyaAnalysis>()

        utterance.statements.forEachIndexed { clauseIndex, statement ->
            val kriyaId = "योग-${clauseIndex + 1}"
            val tinganta = statement.tinganta

            statement.padas.forEach { pada ->
                if (pada is ParsedPada.Avyaya) {
                    if (pada.value == "कृपया") hasPrarthana = true
                    if (pada.value == "मा") hasNishedha = true
                }
            }

            if (tinganta == null) {
                trace += "Nominal clause detected without an explicit verb."
                return@forEachIndexed
            }

            val resolvedDhatu = resolveDhatu(tinganta)
                ?: return VakyaAnalysisResult.Unsupported("Unknown verbal action/dhātu: ${tinganta.segmentedText}")

            val prayoga = SanskritMorphologicalParser.inferPrayoga(tinganta.segmentedText)
            val bindings = extractKarakas(statement.padas, prayoga, conversation, clauseIndex)

            val verbClean = getCleanVerb(tinganta)
            val selectedOp = selectOperation(resolvedDhatu, verbClean, bindings, statement.padas)

            val missingReqs = checkMissingRequirements(resolvedDhatu, selectedOp, bindings)
            if (missingReqs != null) {
                return VakyaAnalysisResult.NeedsClarification(missingReqs)
            }

            kriyas += KriyaAnalysis(
                id = kriyaId,
                dhatuId = resolvedDhatu.id,
                karakas = bindings,
                selectedOperation = selectedOp,
                metadata = mapOf("dhatuName" to resolvedDhatu.upadesha),
            )

            trace += "Analyzed clause $kriyaId with Dhātu ${resolvedDhatu.upadesha} (${resolvedDhatu.id}), operation: $selectedOp"
        }

        val primaryLakara = utterance.statements.firstOrNull()?.tinganta?.let { resolveLakara(it) } ?: Lakara.LOT
        val prayojana = when {
            hasNishedha -> VakyaPrayojana.NISHEDHA
            hasPrarthana -> VakyaPrayojana.PRARTHANA
            primaryLakara == Lakara.LOT -> VakyaPrayojana.AJNA
            else -> VakyaPrayojana.VIDHANA
        }
        val polarity = if (hasNishedha) Polarity.NEGATIVE else Polarity.POSITIVE

        val analysis = VakyaAnalysis(
            speaker = speaker,
            listener = listener,
            sourceText = input.text,
            prayojana = prayojana,
            polarity = polarity,
            lakara = primaryLakara,
            kriyas = kriyas,
        )

        return VakyaAnalysisResult.Analyzed(analysis, trace)
    }

    private fun getCleanVerb(tinganta: ParsedTinganta): String {
        val raw = tinganta.unresolvedIdentifier ?: (tinganta.upasargas.joinToString("") + (tinganta.dhatu ?: ""))
        return raw.trim().replace(Regex("[।॥,.!?+]"), "")
    }

    private fun resolveDhatu(tinganta: ParsedTinganta): Dhatu? {
        val rawDhatu = tinganta.dhatu
        val matches = if (rawDhatu != null) {
            DhatuPatha.all.filter {
                it.upadesha == rawDhatu || it.derivationalSurface == rawDhatu || it.id == rawDhatu
            }
        } else emptyList()

        val executableMatch = matches.find { it.operations.isNotEmpty() }
        if (executableMatch != null) return executableMatch

        val byUpadesha = rawDhatu?.let { DhatuPatha.findByUpadesha(it) } ?: emptyList()
        val execByUpadesha = byUpadesha.find { it.operations.isNotEmpty() }
        if (execByUpadesha != null) return execByUpadesha

        val clean = getCleanVerb(tinganta)

        val explicitMapping = when {
            clean.startsWith("वियोज") -> DhatuPatha.find("07.0007")
            clean.startsWith("योज") || clean == "युङ्क्ते" || clean == "युनक्ति" || clean == "युज्" -> DhatuPatha.find("07.0007")
            clean.startsWith("सम") || clean.startsWith("गुण") || clean.startsWith("गण") -> DhatuPatha.find("10.0391")
            clean.startsWith("हर") || clean.startsWith("हृ") -> DhatuPatha.find("01.0002")
            clean.startsWith("निष्पाद") || clean == "कुरु" || clean.startsWith("करो") || clean.startsWith("कृ") -> DhatuPatha.find("08.0010")
            clean.startsWith("शेष") -> DhatuPatha.find("07.0014")
            clean.startsWith("वर्ध") -> DhatuPatha.find("01.0863")
            clean.startsWith("न्यून") || clean.startsWith("तुल") -> DhatuPatha.find("07.0013")
            clean.startsWith("मूल") -> DhatuPatha.find("01.0607")
            clean.startsWith("भज") -> DhatuPatha.find("01.1153")
            clean == "देहि" || clean.startsWith("ददा") || clean.startsWith("दा") -> DhatuPatha.find("03.0010")
            clean == "पश्य" || clean.startsWith("पश्यति") || clean.startsWith("दृश्") -> DhatuPatha.find("01.1143")
            clean.startsWith("प्रेष") -> DhatuPatha.find("01.1049")
            clean == "स्मर" || clean.startsWith("स्मरति") || clean.startsWith("स्मृ") -> DhatuPatha.find("01.0924")
            else -> null
        }

        if (explicitMapping != null) return explicitMapping

        if (matches.isNotEmpty()) return matches.first()
        if (byUpadesha.isNotEmpty()) return byUpadesha.first()

        return DhatuPatha.all.find {
            it.operations.isNotEmpty() && (clean.startsWith(it.derivationalSurface) || clean.startsWith(it.upadesha))
        } ?: DhatuPatha.all.find {
            clean.startsWith(it.derivationalSurface) || clean.startsWith(it.upadesha)
        }
    }

    private fun resolveLakara(tinganta: ParsedTinganta): Lakara {
        tinganta.lakara?.let { l ->
            return runCatching { Lakara.valueOf(l) }.getOrNull()
                ?: Lakara.entries.firstOrNull { it.upadesha == l }
                ?: Lakara.LOT
        }

        val clean = getCleanVerb(tinganta)

        return if (clean.endsWith("ति") || clean.endsWith("ते") || clean.endsWith("न्ति") || clean.endsWith("न्ते")) {
            Lakara.LAT
        } else {
            Lakara.LOT
        }
    }

    private fun extractKarakas(
        padas: List<ParsedPada>,
        prayoga: Prayoga,
        conversation: SambhashanaContext?,
        clauseIndex: Int,
    ): Map<Karaka, ExecutionExpression> {
        val karakaMap = mutableMapOf<Karaka, MutableList<ExecutionExpression>>()

        padas.forEach { pada ->
            when (pada) {
                is ParsedPada.Subanta -> {
                    val expr = parseSubantaExpr(pada.value, conversation, clauseIndex)
                    val karaka = inferSubantaKaraka(pada.value, prayoga)
                    karakaMap.getOrPut(karaka) { mutableListOf() }.add(expr)
                }

                is ParsedPada.Coordination -> {
                    val exprs = pada.members.map { parseSubantaExpr(it, conversation, clauseIndex) }
                    val memberKaraka = pada.members.firstOrNull()?.let { inferSubantaKaraka(it, prayoga) } ?: Karaka.KARMAN
                    val coordExpr = ExecutionExpression.Coordination(exprs)
                    karakaMap.getOrPut(memberKaraka) { mutableListOf() }.add(coordExpr)
                }

                is ParsedPada.AvyayaKridanta -> {
                    val token = SanskritMorphologicalParser.parseToken(pada.dhatu, prayoga)
                    val expr = ExecutionExpression.Pada(token.stem, token.samjnas + ExecutionSamjna.KRIDANTA)
                    karakaMap.getOrPut(Karaka.KARANA) { mutableListOf() }.add(expr)
                }

                is ParsedPada.Avyaya -> {
                    // Avyayas like 'मा' or 'कृपया' do not bind karakas
                }
            }
        }

        return karakaMap.mapValues { (_, exprs) ->
            if (exprs.size == 1) exprs.single() else ExecutionExpression.Coordination(exprs)
        }
    }

    private fun parseSubantaExpr(
        subanta: ParsedSubanta,
        conversation: SambhashanaContext?,
        clauseIndex: Int,
    ): ExecutionExpression = when (val base = subanta.base) {
        is ParsedNominalBase.Simple -> {
            when (base.kind) {
                SimpleNominalKind.NUMERAL -> {
                    val token = SanskritMorphologicalParser.parseToken(base.value)
                    ExecutionExpression.Pada(token.stem, token.samjnas)
                }

                SimpleNominalKind.RESULT_REFERENCE -> {
                    if (clauseIndex == 0 || base.value.startsWith("पूर्व")) {
                        val previous = conversation?.previousResults?.entries?.lastOrNull { it.key.startsWith("योग-") }?.key
                            ?: conversation?.resultHistory?.lastOrNull()?.id
                        if (previous != null) ExecutionExpression.Reference(previous)
                        else ExecutionExpression.Pada(base.value, setOf(ExecutionSamjna.REFERENCE, ExecutionSamjna.SHABDA))
                    } else {
                        ExecutionExpression.Reference("योग-$clauseIndex")
                    }
                }

                SimpleNominalKind.IDENTIFIER -> {
                    val token = SanskritMorphologicalParser.parseToken(base.value)
                    ExecutionExpression.Pada(token.stem, token.samjnas)
                }
            }
        }

        is ParsedNominalBase.Samasa -> {
            val combined = base.members.joinToString(base.separator) { it.text }
            ExecutionExpression.Pada(combined, setOf(ExecutionSamjna.SAMASA, ExecutionSamjna.SHABDA))
        }

        is ParsedNominalBase.Kridanta -> {
            ExecutionExpression.Pada(base.dhatu, setOf(ExecutionSamjna.KRIDANTA, ExecutionSamjna.SHABDA))
        }

        is ParsedNominalBase.Taddhita -> {
            ExecutionExpression.Pada(base.prakriti.text, setOf(ExecutionSamjna.TADDHITA, ExecutionSamjna.SHABDA))
        }

        is ParsedNominalBase.Stri -> {
            ExecutionExpression.Pada(base.prakriti.text, setOf(ExecutionSamjna.STRI_PRATYAYA, ExecutionSamjna.SHABDA))
        }
    }

    private fun inferSubantaKaraka(
        subanta: ParsedSubanta,
        prayoga: Prayoga,
    ): Karaka {
        val rawText = subanta.text
        val token = SanskritMorphologicalParser.parseToken(rawText, prayoga)
        return token.inferredKaraka
    }

    private fun selectOperation(
        dhatu: Dhatu,
        verbClean: String,
        bindings: Map<Karaka, ExecutionExpression>,
        padas: List<ParsedPada>,
    ): String? {
        if (dhatu.operations.isEmpty()) return null
        if (dhatu.operations.size == 1) return dhatu.operations.first().id

        return when (dhatu.id) {
            "07.0007" -> if (verbClean.startsWith("वियोज")) "सङ्ख्यावियोगः" else "सङ्ख्यायोजनम्"
            "10.0391" -> when {
                verbClean.startsWith("सम") -> "सङ्ख्यासाम्यम्"
                verbClean.startsWith("गण") -> "सङ्ख्यागुणनम्"
                else -> "सङ्ख्यागुणनम्"
            }
            "08.0010" -> {
                val hasIti = padas.any { p ->
                    (p as? ParsedPada.Subanta)?.value?.base?.text == "इति" ||
                    (p as? ParsedPada.Coordination)?.members?.any { m -> m.base.text == "इति" } == true
                }
                if (verbClean.startsWith("निष्पाद")) "पदनिष्पत्तिः"
                else if (hasIti) "संहिताकरणम्"
                else "पदनिष्पत्तिः"
            }
            "07.0013" -> if (verbClean.startsWith("न्यून")) "सङ्ख्यान्यूनत्वम्" else "सङ्ख्यातुलना"
            "01.0002" -> "सङ्ख्याहरणम्"
            "07.0014" -> "सङ्ख्याशेषः"
            "01.0863" -> "सङ्ख्याघातः"
            "01.0607" -> "सङ्ख्यामूलम्"
            "01.1153" -> "सङ्ख्याभागः"
            "03.0010" -> "मूल्यदानम्"
            "01.1143" -> "मूल्यदर्शनम्"
            else -> dhatu.operations.first().id
        }
    }

    private fun checkMissingRequirements(
        dhatu: Dhatu,
        selectedOpId: String?,
        bindings: Map<Karaka, ExecutionExpression>,
    ): String? {
        val op = dhatu.operations.find { it.id == selectedOpId } ?: return null
        for (req in op.signature.requirements) {
            val expr = bindings[req.karaka] ?: return "Required kārakas are missing for dhātu ${dhatu.upadesha}: setOf(${req.karaka})"
            val count = when (expr) {
                is ExecutionExpression.Coordination -> expr.members.size
                else -> 1
            }
            if (count < req.minimumMembers) {
                return "Required kārakas are missing for dhātu ${dhatu.upadesha}: setOf(${req.karaka})"
            }
        }
        return null
    }

    private val ParsedNominalBase.text: String
        get() = when (this) {
            is ParsedNominalBase.Simple -> value
            is ParsedNominalBase.Samasa -> members.joinToString(separator) { it.text }
            is ParsedNominalBase.Kridanta -> dhatu
            is ParsedNominalBase.Taddhita -> prakriti.text
            is ParsedNominalBase.Stri -> prakriti.text
        }

    private val ParsedSubanta.text: String
        get() = base.text + (supPratyaya?.let { "+$it" } ?: "")
}
