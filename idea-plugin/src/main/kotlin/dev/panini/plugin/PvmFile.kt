package dev.panini.plugin

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

class PvmFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, PvmLanguage.INSTANCE) {
    override fun getFileType(): FileType = PvmFileType.INSTANCE
    override fun toString(): String = "PaniniVM File"
}
