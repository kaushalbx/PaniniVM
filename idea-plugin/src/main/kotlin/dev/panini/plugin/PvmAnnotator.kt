package dev.panini.plugin

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement

class PvmAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element.node.elementType == PvmTokenTypes.BAD_CHARACTER) {
            holder.newAnnotation(HighlightSeverity.ERROR, "Unrecognized symbol in PaniniVM script")
                .range(element.textRange)
                .create()
        }
    }
}
