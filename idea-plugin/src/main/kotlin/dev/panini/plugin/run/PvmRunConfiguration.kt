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
import dev.panini.cli.PaniniCli
import dev.panini.execution.ExecutionResult
import com.intellij.execution.configurations.RunConfigurationOptions
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.OutputStream
import java.io.PipedInputStream
import java.io.PipedOutputStream
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
                val input = PipedInputStream()
                private val inputSink = PipedOutputStream(input)
                @Volatile private var childProcess: Process? = null

                override fun destroyProcessImpl() {
                    childProcess?.destroy()
                    inputSink.close()
                }
                override fun detachProcessImpl() {
                    childProcess = null
                    inputSink.close()
                }
                override fun detachIsDefault(): Boolean = false
                override fun getProcessInput(): OutputStream = inputSink

                fun terminate(exitCode: Int) {
                    childProcess = null
                    inputSink.close()
                    notifyProcessTerminated(exitCode)
                }

                fun bind(process: Process) {
                    childProcess = process
                }

                fun consoleStream(outputType: com.intellij.openapi.util.Key<*>): PrintStream = PrintStream(
                    object : OutputStream() {
                        private val line = ByteArrayOutputStream()

                        override fun write(value: Int) {
                            line.write(value)
                            if (value == '\n'.code) flush()
                        }

                        override fun write(bytes: ByteArray, offset: Int, length: Int) {
                            repeat(length) { write(bytes[offset + it].toInt()) }
                        }

                        override fun flush() {
                            if (line.size() == 0) return
                            notifyTextAvailable(line.toString(StandardCharsets.UTF_8), outputType)
                            line.reset()
                        }
                    },
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
                    try {
                        val results = PaniniCli(
                            inputStream = processHandler.input,
                            outputStream = processHandler.consoleStream(ProcessOutputTypes.STDOUT),
                        ).executeScriptFile(file)
                        processHandler.terminate(if (results.any { it is ExecutionResult.Failure }) 1 else 0)
                    } catch (e: Throwable) {
                        processHandler.notifyTextAvailable("Error during execution: ${e.message ?: e::class.simpleName}\n", ProcessOutputTypes.STDERR)
                        processHandler.terminate(1)
                    }
                } else {
                    val basePath = project.basePath ?: ""
                    val isWindows = System.getProperty("os.name").lowercase().contains("win")
                    val gradlewName = if (isWindows) "gradlew.bat" else "gradlew"
                    val gradlewFile = File(basePath, gradlewName)

                    try {
                        processHandler.notifyTextAvailable("=== PaniniVM Script Execution (Compilation): ${file.name} ===\n", ProcessOutputTypes.STDOUT)
                        val process = ProcessBuilder(
                            gradlewFile.absolutePath,
                            ":cli:run",
                            "--args=--eval $path",
                            "--no-daemon"
                        )
                            .directory(File(basePath))
                            .redirectErrorStream(true)
                            .start()
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
