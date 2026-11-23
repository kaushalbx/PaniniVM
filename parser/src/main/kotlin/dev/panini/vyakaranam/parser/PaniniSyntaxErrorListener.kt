package dev.panini.vyakaranam.parser

import org.antlr.v4.kotlinruntime.BaseErrorListener
import org.antlr.v4.kotlinruntime.RecognitionException
import org.antlr.v4.kotlinruntime.Recognizer

class PaniniSyntaxErrorListener : BaseErrorListener() {

    private val mutableErrors = mutableListOf<PaniniSyntaxError>()

    val errors: List<PaniniSyntaxError>
        get() = mutableErrors.toList()

    override fun syntaxError(
        recognizer: Recognizer<*, *>,
        offendingSymbol: Any?,
        line: Int,
        charPositionInLine: Int,
        msg: String,
        e: RecognitionException?,
    ) {
        mutableErrors += PaniniSyntaxError(
            line = line,
            column = charPositionInLine,
            offendingText = offendingSymbol?.toString(),
            message = msg,
            span = (offendingSymbol as? org.antlr.v4.kotlinruntime.Token)?.takeIf { it.startIndex >= 0 && it.stopIndex >= it.startIndex }
                ?.let { dev.panini.vyakaranam.ast.DocumentSourceSpan(it.startIndex, it.stopIndex + 1) },
        )
    }

    fun throwIfAny() {
        if (mutableErrors.isNotEmpty()) {
            throw PaniniParseException(errors)
        }
    }
}
