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
import com.intellij.execution.configurations.RunConfigurationOptions
import java.io.File
import java.io.OutputStream
import java.io.PrintStream
import java.nio.charset.StandardCharsets

class PvmRunConfigurationOptions : LocatableRunConfigurationOptions() {
    var scriptPath: String? by string("")
    var runViaVm: Boolean by property(true)
}

/**
 * PvmRunConfiguration manages interpreter executions for .pvm script files in the IDE.
 */
class PvmRunConfiguration(
    project: Project,
    factory: PvmRunConfigurationFactory,
    name: String
) : LocatableConfigurationBase<PvmRunConfigurationOptions>(project, factory, name) {

    override fun getOptionsClass(): Class<out RunConfigurationOptions> {
        return PvmRunConfigurationOptions::class.java
    }

    override fun getOptions(): PvmRunConfigurationOptions {
        return super.getOptions() as PvmRunConfigurationOptions
    }

    var scriptPath: String?
        get() = options.scriptPath
        set(value) {
            options.scriptPath = value
        }

    var runViaVm: Boolean
        get() = options.runViaVm
        set(value) {
            options.runViaVm = value
        }

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return PvmRunSettingsEditor()
    }

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState {
        return RunProfileState { _, _ ->
            val project = environment.project
            val consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(project).console

            class PvmProcessHandler : ProcessHandler() {
                private val consoleIo = PvmConsoleIo()
                val input get() = consoleIo.input
                @Volatile private var childProcess: Process? = null

                override fun destroyProcessImpl() {
                    childProcess?.destroy()
                    consoleIo.close()
                }
                override fun detachProcessImpl() {
                    childProcess = null
                    consoleIo.close()
                }
                override fun detachIsDefault(): Boolean = false
                override fun getProcessInput(): OutputStream = consoleIo.inputSink

                fun terminate(exitCode: Int) {
                    childProcess = null
                    consoleIo.close()
                    notifyProcessTerminated(exitCode)
                }

                fun bind(process: Process) {
                    childProcess = process
                }

                fun consoleStream(outputType: com.intellij.openapi.util.Key<*>): PrintStream = PrintStream(
                    consoleIo.output { notifyTextAvailable(it, outputType) },
                    true,
                    StandardCharsets.UTF_8,
                )
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

                if (runViaVm) {
                    val exitCode = PvmRunExecutor().execute(
                        file = file,
                        input = processHandler.input,
                        output = processHandler.consoleStream(ProcessOutputTypes.STDOUT),
                    )
                    processHandler.terminate(exitCode)
                } else {
                    val basePath = project.basePath ?: ""

                    try {
                        processHandler.notifyTextAvailable("=== PaniniVM Script Execution (CLI Process): ${file.name} ===\n", ProcessOutputTypes.STDOUT)
                        val process = PvmCliProcessLauncher().start(file, File(basePath))
                        processHandler.bind(process)

                        ApplicationManager.getApplication().executeOnPooledThread {
                            runCatching {
                                processHandler.input.copyTo(process.outputStream)
                                process.outputStream.close()
                            }
                        }

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
            }

            DefaultExecutionResult(consoleView, processHandler)
        }
    }

}
