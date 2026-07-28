package dev.panini.plugin.run

import com.intellij.execution.DefaultExecutionResult
import com.intellij.execution.Executor
import com.intellij.execution.configurations.LocatableConfigurationBase
import com.intellij.execution.configurations.LocatableRunConfigurationOptions
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessOutputTypes
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import java.io.File
import java.io.OutputStream

/**
 * PvmRunConfiguration manages interpreter executions for .pvm script files in the IDE.
 */
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
            val project = environment.project
            val consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(project).console

            class PvmProcessHandler : ProcessHandler() {
                override fun destroyProcessImpl() {}
                override fun detachProcessImpl() {}
                override fun detachIsDefault(): Boolean = false
                override fun getProcessInput(): OutputStream? = null

                fun terminate(exitCode: Int) {
                    notifyProcessTerminated(exitCode)
                }
            }

            val processHandler = PvmProcessHandler()
            consoleView.attachToProcess(processHandler)
            processHandler.startNotify()

            ApplicationManager.getApplication().executeOnPooledThread {
                val path = scriptPath
                if (path == null) {
                    processHandler.notifyTextAvailable("Error: Script path is not specified\n", ProcessOutputTypes.STDERR)
                    processHandler.terminate(1)
                    return@executeOnPooledThread
                }
                val file = File(path)
                if (!file.exists()) {
                    processHandler.notifyTextAvailable("Error: Script file does not exist: $path\n", ProcessOutputTypes.STDERR)
                    processHandler.terminate(1)
                    return@executeOnPooledThread
                }

                val basePath = project.basePath ?: ""
                val isWindows = System.getProperty("os.name").lowercase().contains("win")
                val gradlewName = if (isWindows) "gradlew.bat" else "gradlew"
                val gradlewFile = File(basePath, gradlewName)

                try {
                    val process = ProcessBuilder(
                        gradlewFile.absolutePath,
                        ":cli:run",
                        "--args=--eval $path",
                        "--no-daemon"
                    )
                        .directory(File(basePath))
                        .redirectErrorStream(true)
                        .start()

                    process.inputStream.bufferedReader().use { reader ->
                        var line = reader.readLine()
                        while (line != null) {
                            processHandler.notifyTextAvailable(line + "\n", ProcessOutputTypes.STDOUT)
                            line = reader.readLine()
                        }
                    }

                    val exitCode = process.waitFor()
                    processHandler.terminate(exitCode)
                } catch (e: Throwable) {
                    val stackTrace = java.io.StringWriter().also { e.printStackTrace(java.io.PrintWriter(it)) }.toString()
                    processHandler.notifyTextAvailable("Error during execution:\n$stackTrace\n", ProcessOutputTypes.STDERR)
                    processHandler.terminate(1)
                }
            }

            DefaultExecutionResult(consoleView, processHandler)
        }
    }
}
