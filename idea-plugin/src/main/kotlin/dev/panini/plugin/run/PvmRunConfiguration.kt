package dev.panini.plugin.run

import com.intellij.execution.Executor
import com.intellij.execution.configurations.LocatableConfigurationBase
import com.intellij.execution.configurations.LocatableRunConfigurationOptions
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import dev.panini.main
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream

class PvmRunConfiguration(
    project: Project,
    factory: PvmRunConfigurationFactory,
    name: String
) : LocatableConfigurationBase<LocatableRunConfigurationOptions>(project, factory, name) {

    var scriptPath: String? = null

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return PvmRunSettingsEditor()
    }

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState {
        return RunProfileState { _, _ ->
            val path = scriptPath ?: throw Exception("Script path is not specified")
            val file = File(path)
            if (!file.exists()) {
                throw Exception("Script file does not exist: $path")
            }

            val oldOut = System.out
            val oldErr = System.err
            val baos = ByteArrayOutputStream()
            val ps = PrintStream(baos)
            try {
                System.setOut(ps)
                System.setErr(ps)
                main(arrayOf("--eval", path))
            } finally {
                System.setOut(oldOut)
                System.setErr(oldErr)
            }
            null
        }
    }
}
