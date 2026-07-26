package dev.panini.plugin

import com.intellij.lexer.LexerBase
import com.intellij.psi.tree.IElementType

class PvmLexerAdapter : LexerBase() {
    private var buffer: CharSequence = ""
    private var startOffset: Int = 0
    private var endOffset: Int = 0
    private var currentOffset: Int = 0
    private var tokenStart: Int = 0
    private var tokenEnd: Int = 0
    private var tokenType: IElementType? = null

    private val keywords = setOf("च", "इति", "यदि", "तर्हि", "इत्यादि", "वा", "चेत्")
    private val affixes = setOf("सुँ", "अम्", "औट्", "शस्", "टा", "भ्याम्", "भिसँ", "ङे", "भ्यस्", "ङसिँ", "ङस्", "ओस्", "आम्", "ङि", "सुप्", "तिप्", "तस्", "झि", "सिप्", "थस्", "थ", "मिप्", "वस्", "मस्", "ता", "आताम्", "झ", "थास्", "आथाम्", "ध्वम्", "इट्", "वहि", "महिङ्", "णिच्", "लोट्", "लट्", "लङ्", "विधिलिङ्", "लुट्", "लृट्", "लुङ्")
    private val numbers = setOf("एक", "द्वि", "त्रि", "चतुर्", "पञ्च", "षष्", "सप्त", "अष्ट", "नव", "दश", "शत", "सहस्र")

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        this.buffer = buffer
        this.startOffset = startOffset
        this.endOffset = endOffset
        this.currentOffset = startOffset
        advance()
    }

    override fun getState(): Int = 0
    override fun getTokenType(): IElementType? = tokenType
    override fun getTokenStart(): Int = tokenStart
    override fun getTokenEnd(): Int = tokenEnd
    override fun getBufferSequence(): CharSequence = buffer
    override fun getBufferEnd(): Int = endOffset

    override fun advance() {
        if (currentOffset >= endOffset) {
            tokenType = null
            tokenStart = endOffset
            tokenEnd = endOffset
            return
        }

        tokenStart = currentOffset
        val ch = buffer[currentOffset]

        if (ch.isWhitespace()) {
            while (currentOffset < endOffset && buffer[currentOffset].isWhitespace()) {
                currentOffset++
            }
            tokenEnd = currentOffset
            tokenType = PvmTokenTypes.WHITE_SPACE
            return
        }

        if (ch == '/' && currentOffset + 1 < endOffset && buffer[currentOffset + 1] == '/') {
            while (currentOffset < endOffset && buffer[currentOffset] != '\n') {
                currentOffset++
            }
            tokenEnd = currentOffset
            tokenType = PvmTokenTypes.COMMENT
            return
        }

        if (ch == '+' || ch == '=' || ch == '-') {
            currentOffset++
            tokenEnd = currentOffset
            tokenType = PvmTokenTypes.OPERATOR
            return
        }

        if (ch == '।' || ch == '॥' || ch == ',' || ch == ';') {
            currentOffset++
            tokenEnd = currentOffset
            tokenType = PvmTokenTypes.DELIMITER
            return
        }

        // Read continuous word token
        val start = currentOffset
        while (currentOffset < endOffset) {
            val c = buffer[currentOffset]
            if (c.isWhitespace() || c == '+' || c == '=' || c == '-' || c == '।' || c == '॥' || c == ',' || c == ';') {
                break
            }
            currentOffset++
        }
        val word = buffer.subSequence(start, currentOffset).toString()
        tokenEnd = currentOffset

        tokenType = when {
            keywords.contains(word) -> PvmTokenTypes.KEYWORD
            affixes.contains(word) -> PvmTokenTypes.AFFIX
            numbers.contains(word) -> PvmTokenTypes.NUMBER
            else -> PvmTokenTypes.IDENTIFIER
        }
    }
}
