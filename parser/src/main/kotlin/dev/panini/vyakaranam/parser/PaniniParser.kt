package dev.panini.vyakaranam.parser

import dev.panini.parser.VyakaranamLexer
import dev.panini.parser.VyakaranamParser
import dev.panini.vyakaranam.ast.Ukti
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream
import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.atn.PredictionMode

class PaniniParser(
    private val astBuilder: VyakaranamAstBuilder = VyakaranamAstBuilder(),
) {

    fun parseDocument(source: String): dev.panini.vyakaranam.ast.ProgramDocument {
        if (source.isBlank()) return dev.panini.vyakaranam.ast.ProgramDocument(source, emptyList())
        val context = parseNative(source) { it.document() }
        return astBuilder.buildDocument(context).copy(sourceText = source)
    }

    /** Parses one complete explicit block, independently of physical lines. */
    fun parsePrakriya(source: String): dev.panini.vyakaranam.ast.Prakriya {
        val context = parseNative(source) { it.prakriyaEntry() }
        return astBuilder.buildPrakriya(context.prakriyaBlock()).copy(sourceText = source)
    }

    fun parseRangeDeclaration(source: String): dev.panini.vyakaranam.ast.RangeDeclaration {
        val context = parseNative(source) { it.rangeDeclarationEntry() }
        return astBuilder.buildRangeDeclaration(context.rangeDeclaration()).copy(sourceText = source)
    }

    fun parseScopeDeclaration(source: String): dev.panini.vyakaranam.ast.Scope {
        val context = parseNative(source) { it.scopeDeclarationEntry() }
        return astBuilder.buildScopeDeclaration(context.scopeDeclaration()).copy(sourceText = source)
    }

    private fun <C : ParserRuleContext> parseNative(source: String, entry: (VyakaranamParser) -> C): C {
        require(source.isNotBlank()) { "A declaration cannot be empty." }
        val errors = PaniniSyntaxErrorListener()
        val lexer = VyakaranamLexer(CharStreams.fromString(normalize(source))).apply {
            removeErrorListeners()
            addErrorListener(errors)
        }
        val grammar = VyakaranamParser(CommonTokenStream(lexer)).apply {
            removeErrorListeners()
            addErrorListener(errors)
            interpreter.predictionMode = PredictionMode.LL
        }
        val context = entry(grammar)
        errors.throwIfAny()
        ensureCompletelyParsed(context, grammar)
        return context
    }

    fun parse(source: String): Ukti {
        require(source.isNotBlank()) {
            "उक्तिः रिक्ता न भवितुमर्हति।"
        }

        val normalizedSource = normalize(source)
        val errorListener = PaniniSyntaxErrorListener()

        val lexer = VyakaranamLexer(
            CharStreams.fromString(normalizedSource),
        ).apply {
            removeErrorListeners()
            addErrorListener(errorListener)
        }

        val tokenStream = CommonTokenStream(lexer)

        val parser = VyakaranamParser(tokenStream).apply {
            removeErrorListeners()
            addErrorListener(errorListener)

            interpreter.predictionMode = PredictionMode.LL
        }

        val context = parser.ukti()

        errorListener.throwIfAny()
        ensureCompletelyParsed(context, parser)

        return astBuilder.build(context)
    }

    fun parseOrNull(source: String): Ukti? =
        runCatching { parse(source) }.getOrNull()

    fun validate(source: String): List<PaniniSyntaxError> {
        if (source.isBlank()) {
            return listOf(
                PaniniSyntaxError(
                    line = 1,
                    column = 0,
                    offendingText = null,
                    message = "उक्तिः रिक्ता अस्ति।",
                ),
            )
        }

        return try {
            parse(source)
            emptyList()
        } catch (exception: PaniniParseException) {
            exception.errors
        }
    }

    private fun normalize(source: String): String =
        source
            .replace('\u00A0', ' ')
            .replace("−", "-")

    private fun ensureCompletelyParsed(
        context: ParserRuleContext,
        parser: VyakaranamParser,
    ) {
        if (context.stop == null) {
            throw PaniniParseException(
                listOf(
                    PaniniSyntaxError(
                        line = parser.currentToken?.line ?: 1,
                        column = parser.currentToken?.charPositionInLine ?: 0,
                        offendingText = parser.currentToken?.text,
                        message = "उक्तेः पूर्णं विश्लेषणं न जातम्।",
                    ),
                ),
            )
        }
    }
}
