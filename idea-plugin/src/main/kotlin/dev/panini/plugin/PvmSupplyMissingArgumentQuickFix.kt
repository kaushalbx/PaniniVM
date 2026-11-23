package dev.panini.plugin

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile
import dev.panini.execution.PrakriyaParameter

/** Interactive because a missing argument's value cannot be inferred grammatically. */
class PvmSupplyMissingArgumentQuickFix(
    private val insertionOffset: Int,
    private val parameter: PrakriyaParameter,
) : IntentionAction {
    override fun getText(): String =
        "Supply '${parameter.nameStem}' value (${parameter.type.sanskritName})"

    override fun getFamilyName(): String = "Supply missing PaniniVM procedure argument"

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean =
        editor != null && file is PvmFile && insertionOffset in 0..editor.document.textLength

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        val targetEditor = editor ?: return
        val valueStem = Messages.showInputDialog(
            project,
            "Enter the value pratipadika without its sup ending:",
            "${parameter.nameStem}: ${parameter.type.sanskritName}",
            Messages.getQuestionIcon(),
        )?.trim()?.takeIf(String::isNotEmpty) ?: return
        WriteCommandAction.runWriteCommandAction(project, familyName, null, {
            if (insertionOffset <= targetEditor.document.textLength) {
                targetEditor.document.insertString(insertionOffset, insertion(valueStem))
            }
        }, file)
    }

    /** द्वितीया of the supplied value; a cardinal below विंशति takes the द्वितीया of its own number (त्रि + शस्). */
    internal fun insertion(valueStem: String): String {
        val stem = valueStem.trim()
        val number = dev.panini.sankhya.PrimitiveSankhya.fromAnnotatedPratipadika(stem)?.value
        val sup = when (number?.let(dev.panini.sankhya.SankhyaVacana::requiredFor)) {
            dev.panini.core.Vacana.DVIVACANA -> "औट्"
            dev.panini.core.Vacana.BAHUVACANA -> "शस्"
            else -> "अम्"
        }
        return " ${parameter.nameStem} + ङि $stem + $sup"
    }

    override fun startInWriteAction(): Boolean = false
}
