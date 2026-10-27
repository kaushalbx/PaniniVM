package dev.panini.plugin

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import java.awt.Color
import java.awt.Font

class PvmSyntaxHighlighter : SyntaxHighlighterBase() {

    companion object {
        private fun key(name: String, rgb: Int): TextAttributesKey =
            TextAttributesKey.createTextAttributesKey(
                name,
                TextAttributes(Color(rgb), null, null, null, Font.PLAIN),
            )
        val KEYWORD = key("PVM_KEYWORD", 0xFFB86C)
        val CONTROL_FLOW = key("PVM_CONTROL_FLOW", 0xFF79C6)

        val UPASARGA = key("PVM_UPASARGA", 0xBD93F9)
        val SANADI = key("PVM_SANADI", 0xD6ACFF)
        val LAKARA = key("PVM_LAKARA", 0x82AAFF)
        val TING_AFFIX = key("PVM_TING_AFFIX", 0x75BFFF)
        val SUP_AFFIX = key("PVM_SUP_AFFIX", 0x67D4FF)

        val VIKARANA = key("PVM_VIKARANA", 0x4EDEC5)
        val AGAMA = key("PVM_AGAMA", 0x72DCA5)
        val KRT_AFFIX = key("PVM_KRT_AFFIX", 0xA5E075)
        val TADDHITA_AFFIX = key("PVM_TADDHITA_AFFIX", 0xD4E57A)
        val STRI_AFFIX = key("PVM_STRI_AFFIX", 0xFFD580)

        val LOPA = key("PVM_LOPA", 0xFF7777)
        val DERIVATION_MARKER = key("PVM_DERIVATION_MARKER", 0xE6AD78)
        val ABHYASA_AFFIX = key("PVM_ABHYASA_AFFIX", 0xB5D98A)
        val INTERJECTION = key("PVM_INTERJECTION", 0xFF8FA3)

        val PRAKRIYA_DECLARATION = key("PVM_PRAKRIYA_DECLARATION", 0x50E3C2)
        val PRAKRIYA_REFERENCE = key("PVM_PRAKRIYA_REFERENCE", 0x69D2E7)
        val DOMAIN_DECLARATION = key("PVM_DOMAIN_DECLARATION", 0xC792EA)
        val DOMAIN_REFERENCE = key("PVM_DOMAIN_REFERENCE", 0xA5A6FF)

        val NOMINAL_STEM = key("PVM_NOMINAL_STEM", 0xE6A6A1)
        val NUMBER = key("PVM_NUMBER", 0xF7C66F)
        val DHATU = key("PVM_DHATU", 0x82CFFF)
        val PHALA = key("PVM_PHALA", 0xFF85B3)

        val IDENTIFIER = key("PVM_IDENTIFIER", 0xD6DEEB)
        val OPERATOR = key("PVM_OPERATOR", 0xC5CDD8)
        val DELIMITER = key("PVM_DELIMITER", 0xAAB4C3)
        val COMMENT = key("PVM_COMMENT", 0x8FA98F)
        val BAD_CHARACTER = key("PVM_BAD_CHARACTER", 0xFF5555)

        val KEYWORD_KEYS = arrayOf(KEYWORD)
        val CONTROL_FLOW_KEYS = arrayOf(CONTROL_FLOW)
        val UPASARGA_KEYS = arrayOf(UPASARGA)
        val SANADI_KEYS = arrayOf(SANADI)
        val LAKARA_KEYS = arrayOf(LAKARA)
        val TING_AFFIX_KEYS = arrayOf(TING_AFFIX)
        val SUP_AFFIX_KEYS = arrayOf(SUP_AFFIX)
        val VIKARANA_KEYS = arrayOf(VIKARANA)
        val AGAMA_KEYS = arrayOf(AGAMA)
        val KRT_AFFIX_KEYS = arrayOf(KRT_AFFIX)
        val TADDHITA_AFFIX_KEYS = arrayOf(TADDHITA_AFFIX)
        val STRI_AFFIX_KEYS = arrayOf(STRI_AFFIX)
        val LOPA_KEYS = arrayOf(LOPA)
        val DERIVATION_MARKER_KEYS = arrayOf(DERIVATION_MARKER)
        val ABHYASA_AFFIX_KEYS = arrayOf(ABHYASA_AFFIX)
        val INTERJECTION_KEYS = arrayOf(INTERJECTION)
        val NUMBER_KEYS = arrayOf(NUMBER)
        val DHATU_KEYS = arrayOf(DHATU)
        val PHALA_KEYS = arrayOf(PHALA)
        val IDENTIFIER_KEYS = arrayOf(IDENTIFIER)
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
            PvmTokenTypes.CONTROL_FLOW -> CONTROL_FLOW_KEYS
            PvmTokenTypes.UPASARGA -> UPASARGA_KEYS
            PvmTokenTypes.SANADI -> SANADI_KEYS
            PvmTokenTypes.LAKARA -> LAKARA_KEYS
            PvmTokenTypes.TING_AFFIX -> TING_AFFIX_KEYS
            PvmTokenTypes.SUP_AFFIX -> SUP_AFFIX_KEYS
            PvmTokenTypes.VIKARANA -> VIKARANA_KEYS
            PvmTokenTypes.AGAMA -> AGAMA_KEYS
            PvmTokenTypes.KRT_AFFIX -> KRT_AFFIX_KEYS
            PvmTokenTypes.TADDHITA_AFFIX -> TADDHITA_AFFIX_KEYS
            PvmTokenTypes.STRI_AFFIX -> STRI_AFFIX_KEYS
            PvmTokenTypes.LOPA -> LOPA_KEYS
            PvmTokenTypes.DERIVATION_MARKER -> DERIVATION_MARKER_KEYS
            PvmTokenTypes.ABHYASA_AFFIX -> ABHYASA_AFFIX_KEYS
            PvmTokenTypes.INTERJECTION -> INTERJECTION_KEYS
            PvmTokenTypes.NUMBER -> NUMBER_KEYS
            PvmTokenTypes.DHATU -> DHATU_KEYS
            PvmTokenTypes.PHALA -> PHALA_KEYS
            PvmTokenTypes.IDENTIFIER -> IDENTIFIER_KEYS
            PvmTokenTypes.OPERATOR -> OPERATOR_KEYS
            PvmTokenTypes.LEFT_PAREN, PvmTokenTypes.RIGHT_PAREN, PvmTokenTypes.DELIMITER -> DELIMITER_KEYS
            PvmTokenTypes.COMMENT -> COMMENT_KEYS
            PvmTokenTypes.BAD_CHARACTER -> BAD_CHAR_KEYS
            else -> EMPTY_KEYS
        }
    }
}
