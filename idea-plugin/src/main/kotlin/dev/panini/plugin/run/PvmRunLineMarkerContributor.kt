package dev.panini.plugin.run

import com.intellij.execution.lineMarker.RunLineMarkerContributor
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import dev.panini.plugin.PvmFile
import com.intellij.execution.RunManager
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.runners.ExecutionUtil

class PvmRunLineMarkerContributor : RunLineMarkerContributor() {
    override fun getInfo(element: PsiElement): Info? {
        val psiFile = element.containingFile ?: return null
        if (psiFile !is PvmFile) return null

        // Restrict to the absolute first leaf in the document to prevent duplicate markers
        if (element.textRange.startOffset != 0) return null
        if (PsiTreeUtil.prevLeaf(element) != null) return null

        val runVmAction = object : AnAction("Run via VM", "Run script via in-process VM", AllIcons.Actions.Execute) {
            override fun actionPerformed(e: AnActionEvent) {
                val project = e.project ?: return
                val virtualFile = psiFile.virtualFile ?: return
                val runManager = RunManager.getInstance(project)
                val type = PvmRunConfigurationType()
                val factory = PvmRunConfigurationFactory(type)

                val settings = runManager.createConfiguration("Run ${virtualFile.name} (VM)", factory)
                val pvmConfig = settings.configuration as PvmRunConfiguration
                pvmConfig.scriptPath = virtualFile.path
                pvmConfig.runViaVm = true

                runManager.addConfiguration(settings)
                runManager.selectedConfiguration = settings

                ExecutionUtil.runConfiguration(settings, DefaultRunExecutor.getRunExecutorInstance())
            }
        }

        val runCompileAction = object : AnAction("Run via Compilation", "Run script via Gradle compilation subprocess", AllIcons.Actions.Execute) {
            override fun actionPerformed(e: AnActionEvent) {
                val project = e.project ?: return
                val virtualFile = psiFile.virtualFile ?: return
                val runManager = RunManager.getInstance(project)
                val type = PvmRunConfigurationType()
                val factory = PvmRunConfigurationFactory(type)

                val settings = runManager.createConfiguration("Run ${virtualFile.name} (Compilation)", factory)
                val pvmConfig = settings.configuration as PvmRunConfiguration
                pvmConfig.scriptPath = virtualFile.path
                pvmConfig.runViaVm = false

                runManager.addConfiguration(settings)
                runManager.selectedConfiguration = settings

                ExecutionUtil.runConfiguration(settings, DefaultRunExecutor.getRunExecutorInstance())
            }
        }

        return Info(
            AllIcons.Actions.Execute,
            arrayOf(runVmAction, runCompileAction),
            { "Run script" }
        )
    }
}
