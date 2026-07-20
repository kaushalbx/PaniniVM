package dev.panini.execution

import dev.panini.derivation.Lakara

/**
 * First controlled language slice. It recognizes imperative युजिँर् sentences
 * whose coordinated operands are supported Sanskrit numeral forms.
 */
object ControlledSanskritAnalyzer {
    fun analyze(
        input: SanskritUktiInput,
        conversation: SambhashanaContext? = null,
    ): VakyaAnalysisResult {
        val tokens = tokenize(input.text).toMutableList()
        if (tokens.isEmpty()) return VakyaAnalysisResult.Unsupported("The Sanskrit utterance is empty.")

        if (tokens.firstOrNull() == "हे") {
            if (tokens.size < 3) {
                return VakyaAnalysisResult.NeedsClarification("हे इत्यस्य सम्बोध्यः कः?")
            }
            tokens.removeAt(0)
            tokens.removeAt(0) // The trusted conversation context resolves the addressee identity.
        }

        var prayojana = VakyaPrayojana.AJNA
        var polarity = Polarity.POSITIVE
        if (tokens.firstOrNull() == "कृपया") {
            prayojana = VakyaPrayojana.PRARTHANA
            tokens.removeAt(0)
        }
        if (tokens.firstOrNull() == "मा") {
            prayojana = VakyaPrayojana.NISHEDHA
            polarity = Polarity.NEGATIVE
            tokens.removeAt(0)
        }

        val clauses = splitAt(tokens, "ततः")
        if (clauses.any { it.isEmpty() }) {
            return VakyaAnalysisResult.NeedsClarification("ततः इत्यस्य उभयतः क्रिया अपेक्षिता।")
        }
        val kriyas = mutableListOf<KriyaAnalysis>()
        val normalizedTrace = mutableListOf<String>()
        clauses.forEachIndexed { index, clauseTokens ->
            val clause = clauseTokens.toMutableList()
            val verb = clause.lastOrNull()
            val (dhatuId, selectedOp) = when (verb) {
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
                else -> return VakyaAnalysisResult.Unsupported("Only controlled mathematical and execution clauses are currently analyzed.")
            }
            clause.removeAt(clause.lastIndex)
            val operandForms = clause.filterNot { it == "च" }
            val minOperands = if (selectedOp in setOf("सङ्ख्यागणनम्", "पदनिष्पत्तिः", "सङ्ख्यातुलना", "सङ्ख्यामूलम्", "सङ्ख्यासाम्यम्", "सङ्ख्यान्यूनत्वम्")) 1 else 2
            if (operandForms.size < minOperands) {
                return VakyaAnalysisResult.NeedsClarification("क्रियायै न्यूनातिन्यूनं $minOperands सङ्ख्ये/पदानि अपेक्षिते।")
            }
            val operands = operandForms.map { form ->
                if (form in resultReferences) {
                    if (index == 0) {
                        val previous = conversation?.resultHistory?.lastOrNull()?.id
                            ?: conversation?.previousResults?.keys?.lastOrNull()
                            ?: return VakyaAnalysisResult.NeedsClarification("पूर्वफलं न विद्यते।")
                        ExecutionExpression.Reference(previous)
                    } else {
                        ExecutionExpression.Reference(yogaId(index - 1))
                    }
                } else {
                    val canonical = canonicalNumbers[form]
                    if (canonical != null) {
                        ExecutionExpression.Literal(canonical, setOf(ExecutionSamjna.SANKHYA, ExecutionSamjna.SHABDA))
                    } else {
                        ExecutionExpression.Literal(form, setOf(ExecutionSamjna.SHABDA))
                    }
                }
            }
            val id = yogaId(index)
            kriyas += KriyaAnalysis(
                id = id,
                dhatuId = dhatuId,
                selectedOperation = selectedOp,
                karakas = mapOf(Karaka.KARMAN to ExecutionExpression.Coordination(operands)),
            )
            normalizedTrace += "Recognized $id ($verb) with operands ${operandForms.joinToString()}."
        }

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
            listOf(
                "Recognized योजय with purpose $prayojana and polarity $polarity.",
            ) + normalizedTrace,
        )
    }

    private fun splitAt(tokens: List<String>, separator: String): List<List<String>> {
        val clauses = mutableListOf<MutableList<String>>(mutableListOf())
        tokens.forEach { token ->
            if (token == separator) clauses.add(mutableListOf()) else clauses.last().add(token)
        }
        return clauses
    }

    private fun yogaId(zeroBasedIndex: Int): String {
        return "योग-${DevanagariDigits.render(zeroBasedIndex + 1)}"
    }

    private fun tokenize(text: String): List<String> = text
        .replace(Regex("[।॥,?？！]"), " ")
        .trim()
        .split(Regex("\\s+"))
        .filter { it.isNotBlank() }

    private val canonicalNumbers: Map<String, String> = mapOf(
        "शून्य" to "शून्य", "शून्यम्" to "शून्य", "शून्यं" to "शून्य",
        "एक" to "एक", "एकम्" to "एक", "एकं" to "एक",
        "द्वि" to "द्वि", "द्वे" to "द्वि",
        "त्रि" to "त्रि", "त्रीणि" to "त्रि",
        "चतुर्" to "चतुर्", "चत्वारि" to "चतुर्",
        "पञ्च" to "पञ्च", "षट्" to "षट्", "सप्त" to "सप्त",
        "अष्ट" to "अष्ट", "नव" to "नव", "दश" to "दश",
    )

    private val resultReferences = setOf("फलम्", "फलं", "फले", "पूर्वफलम्", "पूर्वफले")
}
