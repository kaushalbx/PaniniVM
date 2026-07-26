package dev.panini.plugin

import com.intellij.psi.tree.IElementType

object PvmTokenTypes {
    val KEYWORD = IElementType("PVM_KEYWORD", PvmLanguage.INSTANCE)
    val AFFIX = IElementType("PVM_AFFIX", PvmLanguage.INSTANCE)
    val NUMBER = IElementType("PVM_NUMBER", PvmLanguage.INSTANCE)
    val OPERATOR = IElementType("PVM_OPERATOR", PvmLanguage.INSTANCE)
    val DELIMITER = IElementType("PVM_DELIMITER", PvmLanguage.INSTANCE)
    val COMMENT = IElementType("PVM_COMMENT", PvmLanguage.INSTANCE)
    val IDENTIFIER = IElementType("PVM_IDENTIFIER", PvmLanguage.INSTANCE)
    val WHITE_SPACE = IElementType("PVM_WHITE_SPACE", PvmLanguage.INSTANCE)
    val BAD_CHARACTER = IElementType("PVM_BAD_CHARACTER", PvmLanguage.INSTANCE)
}
