package dev.panini.plugin.run

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import dev.panini.plugin.PvmIcons
import javax.swing.Icon

class PvmRunConfigurationType : ConfigurationType {
    companion object {
        const val ID = "PVM_RUN_CONFIGURATION"
    }

    override fun getDisplayName(): String = "PaniniVM"
    override fun getConfigurationTypeDescription(): String = "PaniniVM script execution configuration"
    override fun getIcon(): Icon = PvmIcons.FILE
    override fun getId(): String = ID

    override fun getConfigurationFactories(): Array<ConfigurationFactory> {
        return arrayOf(PvmRunConfigurationFactory(this))
    }
}

class PvmRunConfigurationFactory(type: ConfigurationType) : ConfigurationFactory(type) {
    override fun getId(): String = "PaniniVM"
    override fun createTemplateConfiguration(project: com.intellij.openapi.project.Project): com.intellij.execution.configurations.RunConfiguration {
        return PvmRunConfiguration(project, this, "PaniniVM Script")
    }
}
