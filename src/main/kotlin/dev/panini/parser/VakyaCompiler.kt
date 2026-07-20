package dev.panini.parser

import dev.panini.parser.ast.ParsedUtterance
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
/**
 * Public entry point for compiling segmented Sanskrit source into the
 * parser-level AST.
 *
 * This class currently performs:
 *
 * source
 *   -> lexer
 *   -> parser
 *   -> syntax validation
 *   -> ParsedUtterance
 *
 * Kāraka determination and executable DhatuInvocation construction belong
 * to the later semantic-analysis phase.
 */
class VakyaCompiler(
    private val astBuilder: VakyaAstBuilder = VakyaAstBuilder(),
) {

    fun compile(
        source: String,
        sourceName: String? = null,
    ): ParsedUtterance {
        require(source.isNotBlank()) {
            "Vākya source cannot be blank."
        }

        val errorListener = VakyaErrorListener()

        val input = CharStreams.fromString(source)
        val lexer = VakyaLexer(input)

        /*
         * Capture lexer errors such as an unsupported character instead of
         * allowing ANTLR to print them to stderr.
         */
        lexer.removeErrorListeners()
        lexer.addErrorListener(errorListener)

        val tokenStream = CommonTokenStream(lexer)
        val parser = VakyaParser(tokenStream)

        /*
         * Capture parser errors such as missing suffixes, misplaced '+' or
         * invalid sentence structure.
         */
        parser.removeErrorListeners()
        parser.addErrorListener(errorListener)

        val tree = parser.utterance()

        if (errorListener.hasErrors) {
            throw VakyaSyntaxException(
                diagnostics = errorListener.diagnostics,
                sourceName = sourceName,
            )
        }

        return astBuilder.build(tree)
    }

    /**
     * Useful for callers that want a result instead of an exception.
     */
    fun compileResult(
        source: String,
        sourceName: String? = null,
    ): Result<ParsedUtterance> =
        runCatching {
            compile(
                source = source,
                sourceName = sourceName,
            )
        }
}
