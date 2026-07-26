package dev.panini.plugin

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

class PvmFileType private constructor() : LanguageFileType(PvmLanguage.INSTANCE) {
    companion object {
        @JvmStatic
        val INSTANCE = PvmFileType()
    }

    override fun getName(): String = "PaniniVM"
    override fun getDescription(): String = "PaniniVM script file"
    override fun getDefaultExtension(): String = "pvm"
    override fun getIcon(): Icon = PvmIcons.FILE
}
