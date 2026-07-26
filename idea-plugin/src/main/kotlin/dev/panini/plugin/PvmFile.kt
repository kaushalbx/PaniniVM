package dev.panini.plugin

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

class PvmFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, PvmLanguage) {
    override fun getFileType(): FileType = PvmFileType
    override fun toString(): String = "PaniniVM File"
}
