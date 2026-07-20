package dev.panini.execution

import dev.panini.derivation.Lakara
import dev.panini.execution.parser.VakyaBaseVisitor
import dev.panini.execution.parser.VakyaLexer
import dev.panini.execution.parser.VakyaParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

/**
 * ANTLR4-powered parser for Sanskrit execution utterances.
 * Traverses concrete Pāṇinian morphological AST parse trees (Subanta, Tiṅanta, Kṛdanta, Taddhitānta, Samāsa).
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

            val lexer = VakyaLexer(CharStreams.fromString(text))
            val tokens = CommonTokenStream(lexer)
            val parser = VakyaParser(tokens)

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
        val rawClauses = text.split(Regex("[,\\s]*ततः[,\\s]*|[,\\s]*अथ[,\\s]*|[,\\s]*अनन्तरम्[,\\s]*"))
        return rawClauses.filter { it.isNotBlank() }
    }

    private fun yogaId(zeroBasedIndex: Int): String =
        "योग-${DevanagariDigits.render(zeroBasedIndex + 1)}"

    private class ClauseVisitor(
        private val input: SanskritUktiInput,
        private val conversation: SambhashanaContext?,
        private val clauseIndex: Int,
        private val yogaIdSupplier: (Int) -> String,
    ) : VakyaBaseVisitor<VakyaAnalysisResult>() {

        override fun visitUtterance(ctx: VakyaParser.UtteranceContext?): VakyaAnalysisResult? {
            if (ctx == null) return null
            val vakyaCtx = ctx.vakya().firstOrNull() as? VakyaParser.StandardVakyaContext
                ?: return VakyaAnalysisResult.Unsupported("Empty vakya.")

            val padas: List<VakyaParser.PadaContext> = vakyaCtx.pada() ?: emptyList()
            val tingantaCtx: VakyaParser.TingantaPadaContext? = vakyaCtx.tingantaPada()
            val avyayaKridantaCtx: VakyaParser.AvyayaKridantaPadaContext? = vakyaCtx.avyayaKridantaPada()

            val verbText: String = when {
                tingantaCtx != null -> tingantaCtx.text.trim().replace(Regex("[।॥,.!?+]"), "")
                avyayaKridantaCtx != null -> avyayaKridantaCtx.text.trim().replace(Regex("[।॥,.!?+]"), "")
                else -> return VakyaAnalysisResult.Unsupported("Vakya missing verb (tiṅanta/kṛdanta).")
            }

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

        private fun extractSubantaTexts(ctx: VakyaParser.PadaContext): List<String> {
            return when (ctx) {
                is VakyaParser.CoordinatedPadaContext -> {
                    val coordinated = ctx.coordinatedSubanta()
                    val subantas = coordinated?.subantaPada() ?: emptyList()
                    subantas.map { extractPratipadika(it) }
                }
                is VakyaParser.SingleSubantaPadaContext -> {
                    val subanta = ctx.subantaPada()
                    if (subanta != null) listOf(extractPratipadika(subanta)) else emptyList()
                }
                is VakyaParser.SamasaPadaContext -> {
                    val samasa = ctx.samasaSubanta()
                    if (samasa != null) listOf(samasa.text) else emptyList()
                }
                else -> emptyList()
            }
        }

        private fun extractPratipadika(ctx: VakyaParser.SubantaPadaContext): String {
            return when (ctx) {
                is VakyaParser.NumeralSubantaPadaContext -> ctx.numeralSubanta()?.text ?: ctx.text
                is VakyaParser.ResultSubantaPadaContext -> ctx.resultSubanta()?.text ?: ctx.text
                is VakyaParser.BaseSubantaDerivationContext -> ctx.basePratipadika()?.text ?: ctx.text
                is VakyaParser.KridantaSubantaDerivationContext -> ctx.kridantaPratipadika()?.text ?: ctx.text
                is VakyaParser.TaddhitaSubantaDerivationContext -> ctx.taddhitaPratipadika()?.text ?: ctx.text
                is VakyaParser.SurfaceSubantaContext -> ctx.text
                else -> ctx.text
            }
        }

        private fun resolveVerb(verb: String): Pair<String, String>? = when (verb) {
            "योजय", "युज्+णिच्+लोट्+तिप्" -> "07.0007" to "सङ्ख्यायोजनम्"
            "वियोजय" -> "07.0007" to "सङ्ख्यावियोगः"
            "हर", "विभाजय", "हृ+लोट्+तिप्" -> "01.1046" to "सङ्ख्याहरणम्"
            "गुणय", "गण+णिच्+लोट्+तिप्" -> "10.0391" to "सङ्ख्यागुणनम्"
            "गणय" -> "10.0391" to "सङ्ख्यागणनम्"
            "कुरु", "करोतु", "कृ+लोट्+तिप्" -> "08.0010" to "संहिताकरणम्"
            "निष्पादय" -> "08.0010" to "पदनिष्पत्तिः"
            "शेषय" -> "07.0014" to "सङ्ख्याशेषः"
            "वर्धय" -> "01.0863" to "सङ्ख्याघातः"
            "तुलय" -> "07.0013" to "सङ्ख्यातुलना"
            "मूलय" -> "01.0607" to "सङ्ख्यामूलम्"
            "समय" -> "10.0391" to "सङ्ख्यासाम्यम्"
            "भज" -> "01.1153" to "सङ्ख्याभागः"
            "न्यूनय" -> "07.0013" to "सङ्ख्यान्यूनत्वम्"
            "देहि", "ददातु", "दा+लोट्+तिप्" -> "03.0010" to "मूल्यदानम्"
            "पश्य", "दर्शय", "दृश्+लोट्+तिप्" -> "01.1143" to "मूल्यदर्शनम्"
            "स्मर", "स्मरतु", "स्मरामि", "स्मृ+लोट्+तिप्" -> "01.0601" to "स्मृतिरक्षणम्"
            "प्रेषय", "प्रेषयतु", "प्र+इष्+णिच्+लोट्+तिप्" -> "01.1049" to "बाह्यप्रेषणम्"
            else -> null
        }

        private val resultReferences = setOf("फलम्", "फलं", "फले", "पूर्वफलम्", "पूर्वफलं", "पूर्वफले")
    }
}
