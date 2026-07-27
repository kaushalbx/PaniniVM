package dev.panini.plugin

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import dev.panini.katapayadi.KatapayadiEncoder

/**
 * PvmConvertToKatapayadiIntention converts a positive decimal integer into Katapayadi notation.
 */
class PvmConvertToKatapayadiIntention : PsiElementBaseIntentionAction(), IntentionAction {

    override fun getText(): String = "Convert decimal to Kaṭapayādi"

    override fun getFamilyName(): String = "PaniniVM Numeral Conversions"

    override fun isAvailable(project: Project, editor: Editor, element: PsiElement): Boolean {
        if (element.containingFile !is PvmFile) return false
        val text = element.text.trim()
        val num = text.toLongOrNull() ?: return false
        return num >= 0L
    }

    override fun invoke(project: Project, editor: Editor, element: PsiElement) {
        val text = element.text.trim()
        val num = text.toLongOrNull() ?: return
        val replacement = "कटपय " + KatapayadiEncoder().encode(num)

        val document = editor.document
        val range = element.textRange
        document.replaceString(range.startOffset, range.endOffset, replacement)
    }

    override fun startInWriteAction(): Boolean = true
}
