package dev.panini.plugin

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile

class PvmCanonicalNumeralStemQuickFix(
    private val range: TextRange,
    private val canonical: String,
) : IntentionAction {
    override fun getText(): String = "Replace with canonical numeral stem '$canonical'"

    override fun getFamilyName(): String = "Canonical PaniniVM numeral stems"

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean =
        editor != null && file is PvmFile && range.endOffset <= editor.document.textLength

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        val document = editor?.document ?: return
        if (range.endOffset <= document.textLength) {
            document.replaceString(range.startOffset, range.endOffset, canonical)
        }
    }

    override fun startInWriteAction(): Boolean = true
}
