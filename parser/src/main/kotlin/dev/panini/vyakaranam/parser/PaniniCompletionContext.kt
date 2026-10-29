package dev.panini.vyakaranam.parser

import dev.panini.parser.VyakaranamLexer
import dev.panini.parser.VyakaranamParser
import org.antlr.v4.kotlinruntime.BaseErrorListener
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream
import org.antlr.v4.kotlinruntime.Parser
import org.antlr.v4.kotlinruntime.RecognitionException
import org.antlr.v4.kotlinruntime.Recognizer
import org.antlr.v4.kotlinruntime.Token
import org.antlr.v4.kotlinruntime.atn.PredictionMode

/** Grammar-owned caret expectations for IDE completion of an incomplete document prefix. */
object PaniniCompletionContext {
    private val completionEntries: List<(VyakaranamParser) -> Unit> = listOf(
        { it.vyutpattiTinganta() },
        { it.completionSubantaEntry() },
        { it.completionKridantaEntry() },
        { it.completionUnadyantaEntry() },
        { it.completionAvyayaKridantaEntry() },
        { it.completionSankhyaAbhyasaEntry() },
    )

    private fun morphologyPrefix(sourcePrefix: String): String =
        Regex("(?:[\\p{L}\\p{M}()]+\\s*\\+\\s*)+$")
            .find(sourcePrefix)?.value ?: sourcePrefix

    fun expectedTokenNames(sourcePrefix: String): Set<String> {
        val expectedTypes = linkedSetOf<Int>()
        val morphologyPrefix = morphologyPrefix(sourcePrefix)
        var vocabulary: org.antlr.v4.kotlinruntime.Vocabulary? = null
        completionEntries.forEach { entry ->
            val lexer = VyakaranamLexer(CharStreams.fromString(morphologyPrefix))
            val parser = VyakaranamParser(CommonTokenStream(lexer)).apply {
                removeErrorListeners()
                interpreter.predictionMode = PredictionMode.LL
            }
            vocabulary = parser.vocabulary
            var failedBeforeCaret = false
            parser.addErrorListener(object : BaseErrorListener() {
                override fun syntaxError(
                    recognizer: Recognizer<*, *>,
                    offendingSymbol: Any?,
                    line: Int,
                    charPositionInLine: Int,
                    msg: String,
                    e: RecognitionException?,
                ) {
                    val token = offendingSymbol as? Token ?: return
                    val activeParser = recognizer as? Parser ?: return
                    val atCaretSeparator = token.text == "+" && morphologyPrefix.trimEnd().endsWith('+')
                    if ((token.type == Token.EOF || atCaretSeparator) && !failedBeforeCaret) {
                        expectedTypes += (e?.expectedTokens ?: activeParser.expectedTokens).toList()
                    } else if (token.type != Token.EOF && !atCaretSeparator) {
                        failedBeforeCaret = true
                    }
                }
            })
            runCatching { entry(parser) }
        }
        return expectedTypes.mapNotNullTo(linkedSetOf()) { vocabulary?.getSymbolicName(it) }
    }

    /** True when the canonical grammar consumes [surface] as the next token. */
    fun acceptsNextToken(sourcePrefix: String, surface: String): Boolean {
        if (tokenName(surface) == null) return false
        val candidate = morphologyPrefix(sourcePrefix) + surface
        return completionEntries.any { entry ->
            val parser = VyakaranamParser(CommonTokenStream(VyakaranamLexer(CharStreams.fromString(candidate)))).apply {
                removeErrorListeners()
                interpreter.predictionMode = PredictionMode.LL
            }
            var firstErrorAtEof: Boolean? = null
            parser.addErrorListener(object : BaseErrorListener() {
                override fun syntaxError(
                    recognizer: Recognizer<*, *>,
                    offendingSymbol: Any?,
                    line: Int,
                    charPositionInLine: Int,
                    msg: String,
                    e: RecognitionException?,
                ) {
                    if (firstErrorAtEof == null) {
                        firstErrorAtEof = (offendingSymbol as? Token)?.type == Token.EOF
                    }
                }
            })
            runCatching { entry(parser) }
            firstErrorAtEof != false
        }
    }

    fun tokenName(surface: String): String? {
        val lexer = VyakaranamLexer(CharStreams.fromString(surface))
        val first = lexer.nextToken()
        if (first.type == Token.EOF || lexer.nextToken().type != Token.EOF) return null
        return lexer.vocabulary.getSymbolicName(first.type)
    }
}
