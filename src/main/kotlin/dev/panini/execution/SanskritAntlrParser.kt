import dev.panini.execution.SambhashanaContext
import dev.panini.execution.SanskritUktiInput
import dev.panini.execution.VakyaAnalysisResult
import dev.panini.parser.VakyaAstBuilder
import dev.panini.parser.VakyaLexer
import dev.panini.parser.VakyaParser

import org.antlr.v4.runtime.BaseErrorListener
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer

object SanskritAntlrParser {

    fun parse(
        input: SanskritUktiInput,
        conversation: SambhashanaContext? = null,
    ): VakyaAnalysisResult {
        val charStream = CharStreams.fromString(input.text)

        val lexer = VakyaLexer(charStream)
        val tokenStream = CommonTokenStream(lexer)
        val parser = VakyaParser(tokenStream)

        val syntaxErrors = mutableListOf<String>()

        val errorListener = object : BaseErrorListener() {
            override fun syntaxError(
                recognizer: Recognizer<*, *>?,
                offendingSymbol: Any?,
                line: Int,
                charPositionInLine: Int,
                msg: String?,
                e: RecognitionException?,
            ) {
                syntaxErrors +=
                    "Line $line:$charPositionInLine ${msg.orEmpty()}"
            }
        }

        lexer.removeErrorListeners()
        lexer.addErrorListener(errorListener)

        parser.removeErrorListeners()
        parser.addErrorListener(errorListener)

        val parseTree = parser.utterance() // Replace with your actual root rule.

        if (syntaxErrors.isNotEmpty()) {
            return VakyaAnalysisResult.Unsupported(
                syntaxErrors.joinToString(separator = "\n"),
            )
        }

        return VakyaAstBuilder(
            conversation = conversation,
        ).build(parseTree)
    }
}
