package dev.panini.plugin.run

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.execution.RunManager
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.runners.ExecutionUtil
import com.intellij.icons.AllIcons
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import dev.panini.plugin.PvmFile

class PvmRunLineMarkerProvider : LineMarkerProvider {

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        val psiFile = element.containingFile ?: return null
        if (psiFile !is PvmFile) return null

        // Place green ▶ Run play button icon on line 1 of the script
        if (element.textRange.startOffset != 0) return null

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
