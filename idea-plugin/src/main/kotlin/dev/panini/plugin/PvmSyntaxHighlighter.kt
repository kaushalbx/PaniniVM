package dev.panini.plugin

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType

class PvmSyntaxHighlighter : SyntaxHighlighterBase() {

    companion object {
        val KEYWORD = TextAttributesKey.createTextAttributesKey("PVM_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
        val AFFIX = TextAttributesKey.createTextAttributesKey("PVM_AFFIX", DefaultLanguageHighlighterColors.METADATA)
        val NUMBER = TextAttributesKey.createTextAttributesKey("PVM_NUMBER", DefaultLanguageHighlighterColors.NUMBER)
        val OPERATOR = TextAttributesKey.createTextAttributesKey("PVM_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN)
        val DELIMITER = TextAttributesKey.createTextAttributesKey("PVM_DELIMITER", DefaultLanguageHighlighterColors.SEMICOLON)
        val COMMENT = TextAttributesKey.createTextAttributesKey("PVM_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
        val BAD_CHARACTER = TextAttributesKey.createTextAttributesKey("PVM_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER)

        val KEYWORD_KEYS = arrayOf(KEYWORD)
        val AFFIX_KEYS = arrayOf(AFFIX)
        val NUMBER_KEYS = arrayOf(NUMBER)
        val OPERATOR_KEYS = arrayOf(OPERATOR)
        val DELIMITER_KEYS = arrayOf(DELIMITER)
        val COMMENT_KEYS = arrayOf(COMMENT)
        val BAD_CHAR_KEYS = arrayOf(BAD_CHARACTER)
        val EMPTY_KEYS = emptyArray<TextAttributesKey>()
    }

    override fun getHighlightingLexer(): Lexer {
        return PvmLexerAdapter()
    }

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        return when (tokenType) {
            PvmTokenTypes.KEYWORD -> KEYWORD_KEYS
            PvmTokenTypes.AFFIX -> AFFIX_KEYS
            PvmTokenTypes.NUMBER -> NUMBER_KEYS
            PvmTokenTypes.OPERATOR -> OPERATOR_KEYS
            PvmTokenTypes.DELIMITER -> DELIMITER_KEYS
            PvmTokenTypes.COMMENT -> COMMENT_KEYS
            PvmTokenTypes.BAD_CHARACTER -> BAD_CHAR_KEYS
            else -> EMPTY_KEYS
        }
    }
}
