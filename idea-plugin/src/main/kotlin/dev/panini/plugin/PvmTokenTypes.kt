package dev.panini.plugin

import com.intellij.psi.tree.IElementType

object PvmTokenTypes {
    /**
     * Structural PSI nodes projected from the canonical [PvmFile.canonicalDocument].
     * They deliberately carry no duplicate grammatical state.
     */
    val PRAKRIYA_DECLARATION = IElementType("PVM_PRAKRIYA_DECLARATION", PvmLanguage.INSTANCE)
    val PRAKRIYA_HEADER = IElementType("PVM_PRAKRIYA_HEADER", PvmLanguage.INSTANCE)
    val PRAKRIYA_NAME = IElementType("PVM_PRAKRIYA_NAME", PvmLanguage.INSTANCE)
    val PRAKRIYA_STATEMENT = IElementType("PVM_PRAKRIYA_STATEMENT", PvmLanguage.INSTANCE)
    val SCOPE_DECLARATION = IElementType("PVM_SCOPE_DECLARATION", PvmLanguage.INSTANCE)
    val SCOPE_DOMAIN = IElementType("PVM_SCOPE_DOMAIN", PvmLanguage.INSTANCE)
    val RANGE_DECLARATION = IElementType("PVM_RANGE_DECLARATION", PvmLanguage.INSTANCE)
    val UKTI = IElementType("PVM_UKTI", PvmLanguage.INSTANCE)
    val PROGRAM_ITEM = IElementType("PVM_PROGRAM_ITEM", PvmLanguage.INSTANCE)
    val SUBANTA = IElementType("PVM_SUBANTA", PvmLanguage.INSTANCE)
    val TINGANTA = IElementType("PVM_TINGANTA", PvmLanguage.INSTANCE)

    /** Recovery nodes used while a document is incomplete and cannot form a canonical AST. */
    val UTTERANCE = IElementType("PVM_UTTERANCE", PvmLanguage.INSTANCE)
    val GRAMMATICAL_TERM = IElementType("PVM_GRAMMATICAL_TERM", PvmLanguage.INSTANCE)

    val KEYWORD = IElementType("PVM_KEYWORD", PvmLanguage.INSTANCE)
    val CONTROL_FLOW = IElementType("PVM_CONTROL_FLOW", PvmLanguage.INSTANCE)
    val UPASARGA = IElementType("PVM_UPASARGA", PvmLanguage.INSTANCE)
    val SANADI = IElementType("PVM_SANADI", PvmLanguage.INSTANCE)
    val LAKARA = IElementType("PVM_LAKARA", PvmLanguage.INSTANCE)
    val TING_AFFIX = IElementType("PVM_TING_AFFIX", PvmLanguage.INSTANCE)
    val SUP_AFFIX = IElementType("PVM_SUP_AFFIX", PvmLanguage.INSTANCE)
    val VIKARANA = IElementType("PVM_VIKARANA", PvmLanguage.INSTANCE)
    val AGAMA = IElementType("PVM_AGAMA", PvmLanguage.INSTANCE)
    val KRT_AFFIX = IElementType("PVM_KRT_AFFIX", PvmLanguage.INSTANCE)
    val TADDHITA_AFFIX = IElementType("PVM_TADDHITA_AFFIX", PvmLanguage.INSTANCE)
    val STRI_AFFIX = IElementType("PVM_STRI_AFFIX", PvmLanguage.INSTANCE)
    val LOPA = IElementType("PVM_LOPA", PvmLanguage.INSTANCE)
    val DERIVATION_MARKER = IElementType("PVM_DERIVATION_MARKER", PvmLanguage.INSTANCE)
    val ABHYASA_AFFIX = IElementType("PVM_ABHYASA_AFFIX", PvmLanguage.INSTANCE)
    val INTERJECTION = IElementType("PVM_INTERJECTION", PvmLanguage.INSTANCE)
    val NUMBER = IElementType("PVM_NUMBER", PvmLanguage.INSTANCE)
    val DHATU = IElementType("PVM_DHATU", PvmLanguage.INSTANCE)
    val PHALA = IElementType("PVM_PHALA", PvmLanguage.INSTANCE)
    val OPERATOR = IElementType("PVM_OPERATOR", PvmLanguage.INSTANCE)
    val LEFT_PAREN = IElementType("PVM_LEFT_PAREN", PvmLanguage.INSTANCE)
    val RIGHT_PAREN = IElementType("PVM_RIGHT_PAREN", PvmLanguage.INSTANCE)
    val DELIMITER = IElementType("PVM_DELIMITER", PvmLanguage.INSTANCE)
    val COMMENT = IElementType("PVM_COMMENT", PvmLanguage.INSTANCE)
    val IDENTIFIER = IElementType("PVM_IDENTIFIER", PvmLanguage.INSTANCE)
    val WHITE_SPACE = IElementType("PVM_WHITE_SPACE", PvmLanguage.INSTANCE)
    val BAD_CHARACTER = IElementType("PVM_BAD_CHARACTER", PvmLanguage.INSTANCE)

    val SEMANTIC_SYMBOLS = setOf(IDENTIFIER, DHATU, PHALA, NUMBER)
}
