package dev.panini.execution

import dev.panini.derivation.Lakara
import dev.panini.execution.parser.SanskritExecutionBaseVisitor
import dev.panini.execution.parser.SanskritExecutionLexer
import dev.panini.execution.parser.SanskritExecutionParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

/**
 * ANTLR4-powered parser for Sanskrit execution utterances.
 * Uses SanskritExecutionLexer and SanskritExecutionParser.
 */
object SanskritAntlrParser {

    fun parse(
        input: SanskritUktiInput,
        conversation: SambhashanaContext? = null,
    ): VakyaAnalysisResult {
        val clausesText = splitClauses(input.text)
        val kriyas = mutableListOf<KriyaAnalysis>()
        val trace = mutableListOf<String>("ANTLR4 parsed utterance successfully.")

        clausesText.forEachIndexed { index, clauseText ->
            var text = clauseText.trim()
            if (text.startsWith("हे ")) {
                val spaceIdx = text.indexOf(' ', 3)
                if (spaceIdx != -1) {
                    text = text.substring(spaceIdx + 1).trim()
                }
            }
            if (!text.endsWith("।") && !text.endsWith("॥") && !text.endsWith(".")) {
                text += "।"
            }

            val lexer = SanskritExecutionLexer(CharStreams.fromString(text))
            val tokens = CommonTokenStream(lexer)
            val parser = SanskritExecutionParser(tokens)

            val tree = parser.utterance()
            if (parser.numberOfSyntaxErrors > 0) {
                return VakyaAnalysisResult.Unsupported("Syntax error in Sanskrit utterance '${input.text}'.")
            }

            val visitor = ClauseVisitor(input, conversation, index, ::yogaId)
            val clauseResult = visitor.visitUtterance(tree)
            val analyzed = clauseResult as? VakyaAnalysisResult.Analyzed
                ?: return clauseResult ?: VakyaAnalysisResult.Unsupported("Failed to parse clause.")

            kriyas.addAll(analyzed.analysis.kriyas)
            trace.addAll(analyzed.trace)
        }

        val isPrarthana = input.text.contains("कृपया")
        val isNishedha = input.text.contains("मा ") || input.text.startsWith("मा") || input.text.endsWith("मा")
        val prayojana = when {
            isPrarthana -> VakyaPrayojana.PRARTHANA
            isNishedha -> VakyaPrayojana.NISHEDHA
            else -> VakyaPrayojana.AJNA
        }
        val polarity = if (isNishedha) Polarity.NEGATIVE else Polarity.POSITIVE

        return VakyaAnalysisResult.Analyzed(
            VakyaAnalysis(
                speaker = input.speaker,
                listener = input.listener,
                sourceText = input.text,
                prayojana = prayojana,
                polarity = polarity,
                lakara = Lakara.LOT,
                kriyas = kriyas,
            ),
            trace,
        )
    }

    private fun splitClauses(text: String): List<String> {
        val rawClauses = text.split(Regex("[,\\s]*ततः[,\\s]*"))
        return rawClauses.filter { it.isNotBlank() }
    }

    private fun yogaId(zeroBasedIndex: Int): String =
        "योग-${DevanagariDigits.render(zeroBasedIndex + 1)}"

    private class ClauseVisitor(
        private val input: SanskritUktiInput,
        private val conversation: SambhashanaContext?,
        private val clauseIndex: Int,
        private val yogaIdSupplier: (Int) -> String,
    ) : SanskritExecutionBaseVisitor<VakyaAnalysisResult>() {

        override fun visitUtterance(ctx: SanskritExecutionParser.UtteranceContext?): VakyaAnalysisResult? {
            if (ctx == null) return null
            val vakyaCtx: SanskritExecutionParser.VakyaContext = ctx.vakya()
                ?: return VakyaAnalysisResult.Unsupported("Empty vakya.")
            val padas: List<SanskritExecutionParser.PadaContext> = vakyaCtx.pada() ?: emptyList()
            val tingantaCtx: SanskritExecutionParser.TingantaPadaContext = vakyaCtx.tingantaPada()
                ?: return VakyaAnalysisResult.Unsupported("Vakya missing verb (tiṅanta).")

            val verbText: String = tingantaCtx.text.trim().replace(Regex("[।॥,.!?+]"), "")
            val (dhatuId, selectedOp) = resolveVerb(verbText)
                ?: return VakyaAnalysisResult.Unsupported("Verb '$verbText' is not a supported mathematical or execution verb.")

            val operandTexts: List<String> = padas.flatMap { extractSubantaTexts(it) }
                .map { item: String -> item.replace(Regex("[।॥,.!?+]"), "").trim() }
                .filter { item: String -> item.isNotBlank() && item != "च" && item != "+" && item != "कृपया" && item != "मा" }

            val minOperands = if (selectedOp in setOf("सङ्ख्यागणनम्", "पदनिष्पत्तिः", "सङ्ख्यातुलना", "सङ्ख्यामूलम्", "सङ्ख्यासाम्यम्", "सङ्ख्यान्यूनत्वम्", "मूल्यदानम्", "मूल्यदर्शनम्")) 1 else 2
            if (operandTexts.size < minOperands) {
                return VakyaAnalysisResult.NeedsClarification("क्रियायै न्यूनातिन्यूनं $minOperands सङ्ख्ये/पदानि अपेक्षिते।")
            }

            val parsedTokens = operandTexts.map { tokenText: String -> SanskritMorphologicalParser.parseToken(tokenText) }
            val karakaMap = SanskritMorphologicalParser.groupKarakas(
                parsedTokens,
                resultReferences,
                conversation,
                yogaIdSupplier,
                clauseIndex,
            )

            val id = yogaIdSupplier(clauseIndex)
            val kriya = KriyaAnalysis(
                id = id,
                dhatuId = dhatuId,
                selectedOperation = selectedOp,
                karakas = karakaMap,
            )

            val isPrarthana = input.text.contains("कृपया")
            val isNishedha = input.text.contains("मा ") || input.text.startsWith("मा") || input.text.endsWith("मा")
            val prayojana = when {
                isPrarthana -> VakyaPrayojana.PRARTHANA
                isNishedha -> VakyaPrayojana.NISHEDHA
                else -> VakyaPrayojana.AJNA
            }
            val polarity = if (isNishedha) Polarity.NEGATIVE else Polarity.POSITIVE

            return VakyaAnalysisResult.Analyzed(
                VakyaAnalysis(
                    speaker = input.speaker,
                    listener = input.listener,
                    sourceText = input.text,
                    prayojana = prayojana,
                    polarity = polarity,
                    lakara = Lakara.LOT,
                    kriyas = listOf(kriya),
                ),
                listOf("Recognized $id ($verbText) with morphological kārakas $karakaMap."),
            )
        }

        private fun extractSubantaTexts(ctx: SanskritExecutionParser.PadaContext): List<String> {
            val coordinated: SanskritExecutionParser.CoordinatedSubantaContext? = ctx.coordinatedSubanta()
            if (coordinated != null) {
                val subantas: List<SanskritExecutionParser.SubantaPadaContext> = coordinated.subantaPada() ?: emptyList()
                return subantas.map { subanta -> extractPratipadika(subanta) }
            }
            val subanta: SanskritExecutionParser.SubantaPadaContext? = ctx.subantaPada()
            if (subanta != null) {
                return listOf(extractPratipadika(subanta))
            }
            return emptyList()
        }

        private fun extractPratipadika(ctx: SanskritExecutionParser.SubantaPadaContext): String {
            val pratipadika: SanskritExecutionParser.PratipadikaContext? = ctx.pratipadika()
            if (pratipadika != null) return pratipadika.text
            return ctx.text
        }

        private fun resolveVerb(verb: String): Pair<String, String>? = when (verb) {
            "योजय" -> "07.0007" to "सङ्ख्यायोजनम्"
            "वियोजय" -> "07.0007" to "सङ्ख्यावियोगः"
            "हर", "विभाजय" -> "01.1046" to "सङ्ख्याहरणम्"
            "गुणय" -> "10.0391" to "सङ्ख्यागुणनम्"
            "गणय" -> "10.0391" to "सङ्ख्यागणनम्"
            "कुरु", "करोतु" -> "08.0010" to "संहिताकरणम्"
            "निष्पादय" -> "08.0010" to "पदनिष्पत्तिः"
            "शेषय" -> "07.0014" to "सङ्ख्याशेषः"
            "वर्धय" -> "01.0863" to "सङ्ख्याघातः"
            "तुलय" -> "07.0013" to "सङ्ख्यातुलना"
            "मूलय" -> "01.0607" to "सङ्ख्यामूलम्"
            "समय" -> "10.0391" to "सङ्ख्यासाम्यम्"
            "भज" -> "01.1153" to "सङ्ख्याभागः"
            "न्यूनय" -> "07.0013" to "सङ्ख्यान्यूनत्वम्"
            "देहि", "ददातु" -> "03.0010" to "मूल्यदानम्"
            "पश्य", "दर्शय" -> "01.1143" to "मूल्यदर्शनम्"
            else -> null
        }

        private val resultReferences = setOf("फलम्", "फलं", "फले", "पूर्वफलम्", "पूर्वफलं", "पूर्वफले")
    }
}
