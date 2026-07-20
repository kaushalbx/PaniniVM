package dev.panini.execution

import dev.panini.parser.VakyaCompiler

/**
 * Controlled language analyzer powered by ANTLR4 parser and
 * morphological Subanta & Kāraka analysis.
 */
object ControlledSanskritAnalyzer {
    fun analyze(
        input: SanskritUktiInput,
        conversation: SambhashanaContext? = null,
    ): VakyaAnalysisResult {
        if (input.text.isBlank()) {
            return VakyaAnalysisResult.Unsupported("The Sanskrit utterance is empty.")
        }
        return SanskritAntlrParser.parse(input, conversation)
    }
}
