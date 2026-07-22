package dev.panini.parser

import dev.panini.parser.ast.ParsedUtterance
import dev.panini.vyakaranam.parser.PaniniParseException
import dev.panini.vyakaranam.parser.PaniniParser
import java.io.File

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
    private val parser: PaniniParser = PaniniParser(),
) {

    fun compile(
        source: String,
        sourceName: String? = null,
    ): ParsedUtterance {
        require(source.isNotBlank()) {
            "Vākya source cannot be blank."
        }

        val ukti = try {
            parser.parse(source)
        } catch (exception: PaniniParseException) {
            throw VakyaSyntaxException(
                diagnostics = exception.errors.map {
                    VakyaSyntaxDiagnostic(
                        line = it.line,
                        column = it.column,
                        offendingText = it.offendingText,
                        message = it.message,
                    )
                },
                sourceName = sourceName,
            )
        }

        return astBuilder.build(ukti)
    }

    fun compileFile(file: File): ParsedUtterance =
        compile(file.readText(), sourceName = file.name)

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
