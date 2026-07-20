package dev.panini.parser

import org.antlr.v4.runtime.BaseErrorListener
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer
import org.antlr.v4.runtime.Token


/**
 * Collects lexer and parser diagnostics instead of printing them to stderr.
 *
 * Create a separate listener for each compilation because the listener is
 * mutable while parsing.
 */
class VakyaErrorListener : BaseErrorListener() {

    private val mutableDiagnostics =
        mutableListOf<VakyaSyntaxDiagnostic>()

    val diagnostics: List<VakyaSyntaxDiagnostic>
        get() = mutableDiagnostics.toList()

    val hasErrors: Boolean
        get() = mutableDiagnostics.isNotEmpty()

    override fun syntaxError(
        recognizer: Recognizer<*, *>,
        offendingSymbol: Any?,
        line: Int,
        charPositionInLine: Int,
        msg: String,
        e: RecognitionException?
    ) {
        mutableDiagnostics += VakyaSyntaxDiagnostic(
            line = line,
            column = charPositionInLine,
            message = msg,
            offendingText = offendingText(offendingSymbol),
        )
    }

    private fun offendingText(
        offendingSymbol: Any?,
    ): String? =
        when (offendingSymbol) {
            is Token -> offendingSymbol.text
            null -> null
            else -> offendingSymbol.toString()
        }?.takeIf(String::isNotBlank)
}
