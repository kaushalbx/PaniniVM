package dev.panini.plugin

import com.intellij.psi.tree.IElementType

object PvmTokenTypes {
    val KEYWORD = IElementType("PVM_KEYWORD", PvmLanguage)
    val AFFIX = IElementType("PVM_AFFIX", PvmLanguage)
    val NUMBER = IElementType("PVM_NUMBER", PvmLanguage)
    val OPERATOR = IElementType("PVM_OPERATOR", PvmLanguage)
    val DELIMITER = IElementType("PVM_DELIMITER", PvmLanguage)
    val COMMENT = IElementType("PVM_COMMENT", PvmLanguage)
    val IDENTIFIER = IElementType("PVM_IDENTIFIER", PvmLanguage)
    val WHITE_SPACE = IElementType("PVM_WHITE_SPACE", PvmLanguage)
    val BAD_CHARACTER = IElementType("PVM_BAD_CHARACTER", PvmLanguage)
}
