package dev.panini.plugin.run

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement
import dev.panini.plugin.PvmFile

/**
 * PvmRunConfigurationProducer creates run configurations from active .pvm script contexts.
 */
class PvmRunConfigurationProducer : LazyRunConfigurationProducer<PvmRunConfiguration>() {

    override fun getConfigurationFactory(): ConfigurationFactory {
        return PvmRunConfigurationType().configurationFactories[0]
    }

    override fun setupConfigurationFromContext(
        configuration: PvmRunConfiguration,
        context: ConfigurationContext,
        sourceElement: Ref<PsiElement>
    ): Boolean {
        val psiFile = context.psiLocation?.containingFile ?: return false
        if (psiFile !is PvmFile) return false

        val virtualFile = psiFile.virtualFile ?: return false
        configuration.scriptPath = virtualFile.path
        configuration.name = virtualFile.name
        return true
    }

    override fun isConfigurationFromContext(
        configuration: PvmRunConfiguration,
        context: ConfigurationContext
    ): Boolean {
        val psiFile = context.psiLocation?.containingFile ?: return false
        if (psiFile !is PvmFile) return false
        val virtualFile = psiFile.virtualFile ?: return false
        return configuration.scriptPath == virtualFile.path
    }
}
