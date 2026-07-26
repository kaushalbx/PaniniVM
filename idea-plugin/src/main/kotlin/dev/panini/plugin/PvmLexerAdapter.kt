package dev.panini.plugin

import com.intellij.lexer.LexerBase
import com.intellij.psi.tree.IElementType
import dev.panini.parser.VyakaranamLexer
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.Token

class PvmLexerAdapter : LexerBase() {
    private var buffer: CharSequence = ""
    private var startOffset: Int = 0
    private var endOffset: Int = 0
    private var tokens: List<Token> = emptyList()
    private var tokenIndex: Int = 0
    private var currentTokenType: IElementType? = null
    private var currentTokenStart: Int = 0
    private var currentTokenEnd: Int = 0

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        this.buffer = buffer
        this.startOffset = startOffset
        this.endOffset = endOffset
        val text = buffer.subSequence(startOffset, endOffset).toString()
        if (text.isEmpty()) {
            tokens = emptyList()
            tokenIndex = 0
            currentTokenType = null
            return
        }

        val lexer = VyakaranamLexer(CharStreams.fromString(text))
        tokens = lexer.allTokens
        tokenIndex = 0
        advance()
    }

    override fun getState(): Int = 0
    override fun getTokenType(): IElementType? = currentTokenType
    override fun getTokenStart(): Int = currentTokenStart
    override fun getTokenEnd(): Int = currentTokenEnd
    override fun getBufferSequence(): CharSequence = buffer
    override fun getBufferEnd(): Int = endOffset

    override fun advance() {
        if (tokenIndex >= tokens.size) {
            currentTokenType = null
            currentTokenStart = endOffset
            currentTokenEnd = endOffset
            return
        }

        val token = tokens[tokenIndex++]
        currentTokenStart = startOffset + token.startIndex
        currentTokenEnd = startOffset + token.stopIndex + 1

        currentTokenType = mapAntlrTokenType(token.type)
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
            name == "WS" || name == "LINE_COMMENT" -> PvmTokenTypes.COMMENT
            else -> PvmTokenTypes.IDENTIFIER
        }
    }
}
