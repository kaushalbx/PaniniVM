package dev.panini.plugin.run

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.execution.RunManager
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.runners.ExecutionUtil
import com.intellij.icons.AllIcons
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import dev.panini.plugin.PvmFile

/**
 * PvmRunLineMarkerProvider adds a single Run play gutter icon at the first line of any PvmFile.
 */
class PvmRunLineMarkerProvider : LineMarkerProvider {

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        val psiFile = element.containingFile ?: return null
        if (psiFile !is PvmFile) return null

        // Restrict to the absolute first leaf in the document to prevent duplicate markers
        if (element.textRange.startOffset != 0) return null
        if (PsiTreeUtil.prevLeaf(element) != null) return null

        return LineMarkerInfo(
            element,
            element.textRange,
            AllIcons.Actions.Execute,
            { "Run '${psiFile.name}'" },
            { _, _ ->
                val project = psiFile.project
                val virtualFile = psiFile.virtualFile ?: return@LineMarkerInfo
                val runManager = RunManager.getInstance(project)
                val type = PvmRunConfigurationType()
                val factory = PvmRunConfigurationFactory(type)

                val settings = runManager.createConfiguration("Run ${virtualFile.name}", factory)
                val pvmConfig = settings.configuration as PvmRunConfiguration
                pvmConfig.scriptPath = virtualFile.path

                runManager.addConfiguration(settings)
                runManager.selectedConfiguration = settings

                ExecutionUtil.runConfiguration(settings, DefaultRunExecutor.getRunExecutorInstance())
            },
            GutterIconRenderer.Alignment.LEFT,
            { "Run script" }
        )
    }
}
