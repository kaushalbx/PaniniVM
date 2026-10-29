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

    internal fun insertion(valueStem: String): String =
        " ${parameter.nameStem} + ङि ${valueStem.trim()} + अम्"

    override fun startInWriteAction(): Boolean = false
}
