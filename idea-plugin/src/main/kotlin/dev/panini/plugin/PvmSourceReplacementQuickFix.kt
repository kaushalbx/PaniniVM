package dev.panini.plugin

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile

class PvmSourceReplacementQuickFix(
    private val range: TextRange,
    private val replacement: String,
) : IntentionAction {
    override fun getText(): String = "Replace with '$replacement'"

    override fun getFamilyName(): String = "Simplify PaniniVM source"

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean =
        editor != null && file is PvmFile && range.endOffset <= editor.document.textLength

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        val document = editor?.document ?: return
        if (range.endOffset <= document.textLength) {
            document.replaceString(range.startOffset, range.endOffset, replacement)
        }
    }

    override fun startInWriteAction(): Boolean = true
}
