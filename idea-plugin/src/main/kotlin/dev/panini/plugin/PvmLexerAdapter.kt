package dev.panini.plugin

import com.intellij.lexer.LexerBase
import com.intellij.psi.tree.IElementType
import dev.panini.parser.VyakaranamLexer
import org.antlr.v4.kotlinruntime.CharStreams

class PvmLexerAdapter : LexerBase() {
    private var buffer: CharSequence = ""
    private var startOffset: Int = 0
    private var endOffset: Int = 0

    private data class TokenSpan(val start: Int, val end: Int, val type: IElementType)
    private var spans: List<TokenSpan> = emptyList()
    private var spanIndex: Int = 0

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        this.buffer = buffer
        this.startOffset = startOffset
        this.endOffset = endOffset
        this.spanIndex = 0

        val totalLength = endOffset - startOffset
        if (totalLength <= 0) {
            spans = emptyList()
            return
        }

        val text = buffer.subSequence(startOffset, endOffset).toString()
        val antlrTokens = try {
            val lexer = VyakaranamLexer(CharStreams.fromString(text))
            lexer.allTokens
        } catch (_: Throwable) {
            emptyList()
        }

        val computedSpans = mutableListOf<TokenSpan>()
        var cursor = 0

        for (token in antlrTokens) {
            val tStart = token.startIndex.coerceIn(0, totalLength)
            val tEnd = (token.stopIndex + 1).coerceIn(tStart, totalLength)

            // Fill gap before token with WHITE_SPACE
            if (cursor < tStart) {
                computedSpans.add(TokenSpan(startOffset + cursor, startOffset + tStart, PvmTokenTypes.WHITE_SPACE))
            }

            if (tEnd > tStart) {
                computedSpans.add(TokenSpan(startOffset + tStart, startOffset + tEnd, mapAntlrTokenType(token.type)))
                cursor = tEnd
            }
        }

        // Fill trailing gap if any
        if (cursor < totalLength) {
            computedSpans.add(TokenSpan(startOffset + cursor, endOffset, PvmTokenTypes.WHITE_SPACE))
        }

        spans = if (computedSpans.isNotEmpty()) computedSpans else listOf(TokenSpan(startOffset, endOffset, PvmTokenTypes.IDENTIFIER))
    }

    override fun getState(): Int = 0
    override fun getTokenType(): IElementType? = if (spanIndex < spans.size) spans[spanIndex].type else null
    override fun getTokenStart(): Int = if (spanIndex < spans.size) spans[spanIndex].start else endOffset
    override fun getTokenEnd(): Int = if (spanIndex < spans.size) spans[spanIndex].end else endOffset
    override fun getBufferSequence(): CharSequence = buffer
    override fun getBufferEnd(): Int = endOffset

    override fun advance() {
        if (spanIndex < spans.size) {
            spanIndex++
        }
    }

    private fun mapAntlrTokenType(type: Int): IElementType {
        val symbolicName = runCatching { VyakaranamLexer(CharStreams.fromString("")).vocabulary.getSymbolicName(type) }.getOrNull()
            ?: return PvmTokenTypes.IDENTIFIER

        val name = symbolicName.toString()

        return when {
            name == "PLUS" || name == "SAMASA_SEPARATOR" -> PvmTokenTypes.OPERATOR
            name == "COMMA" || name == "DANDA" || name == "LPAREN" || name == "RPAREN" -> PvmTokenTypes.DELIMITER
            name in setOf(
                "HE", "BHOH", "CHA", "VAA", "ATHA", "TATAH", "ANANTARAM", "KINTU",
                "ATAH", "YATAH", "MAA", "NA", "ITI", "API", "EVA", "TU_AVYAYA",
                "HI", "KHALU", "NANU", "YATHA", "TATHA", "YADA", "TADA", "YATRA",
                "TATRA", "KADA", "KUTRA", "SARVATRA", "KATHAM", "KUTAH", "KRPAYA",
                "SAHASAA", "SHANAIH", "PUNAH", "NYUNATAYA", "ADYA", "SHVAH", "HYAH",
                "YADI", "TARHI", "ANYATHA", "YAVAT", "TAVAT"
            ) -> PvmTokenTypes.KEYWORD
            name.startsWith("LAT") || name.startsWith("LIT") || name.startsWith("LUT") ||
            name.startsWith("LRT") || name.startsWith("LET") || name.startsWith("LOT") ||
            name.startsWith("LANG") || name.startsWith("LIN") || name.startsWith("LUNG") ||
            name.startsWith("LRNG") || name.startsWith("SUP_") ||
            name in setOf(
                "TIP", "TAS", "JHI", "SIP", "THAS", "THA", "MIP", "VAS", "MAS",
                "TA", "ATAAM", "JHA", "THAS_A", "ATHAAM", "DHVAM", "IT", "VAHI",
                "MAHING", "NIC", "SAN"
            ) -> PvmTokenTypes.AFFIX
            name == "WS" -> PvmTokenTypes.WHITE_SPACE
            name == "LINE_COMMENT" -> PvmTokenTypes.COMMENT
            else -> PvmTokenTypes.IDENTIFIER
        }
    }
}
