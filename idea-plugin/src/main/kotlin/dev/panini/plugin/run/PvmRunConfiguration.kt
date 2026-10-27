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
import com.intellij.execution.configurations.RuntimeConfigurationError
import java.io.File
import java.io.OutputStream
import java.io.PrintStream
import java.nio.charset.StandardCharsets
import java.util.concurrent.atomic.AtomicBoolean

internal class PvmProcessHandler : ProcessHandler() {
    private val consoleIo = PvmConsoleIo()
    private val cancellation = AtomicBoolean(false)
    private val finished = AtomicBoolean(false)
    val input get() = consoleIo.input
    val cancellationRequested: Boolean get() = cancellation.get()
    @Volatile private var childProcess: Process? = null

    override fun destroyProcessImpl() {
        cancellation.set(true)
        childProcess?.destroy()
        consoleIo.close()
        terminate(CANCELLED_EXIT_CODE)
    }

    override fun detachProcessImpl() {
        cancellation.set(true)
        childProcess = null
        consoleIo.close()
        if (finished.compareAndSet(false, true)) notifyProcessDetached()
    }

    override fun detachIsDefault(): Boolean = false
    override fun getProcessInput(): OutputStream = consoleIo.inputSink

    fun terminate(exitCode: Int) {
        childProcess = null
        consoleIo.close()
        if (finished.compareAndSet(false, true)) notifyProcessTerminated(exitCode)
    }

    fun bind(process: Process): Boolean {
        if (cancellationRequested) {
            process.destroy()
            return false
        }
        childProcess = process
        if (cancellationRequested) {
            childProcess = null
            process.destroy()
            return false
        }
        return true
    }

    fun consoleStream(outputType: com.intellij.openapi.util.Key<*>): PrintStream = PrintStream(
        consoleIo.output { notifyTextAvailable(it, outputType) },
        true,
        StandardCharsets.UTF_8,
    )

    companion object {
        const val CANCELLED_EXIT_CODE = 130
    }
}

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

    internal fun resolvedScriptFile(): File? {
        val configured = scriptPath?.trim().orEmpty()
        if (configured.isEmpty()) return null
        val file = File(configured)
        return if (file.isAbsolute) file else project.basePath?.let { File(it, configured) } ?: file
    }

    override fun checkConfiguration() {
        val file = resolvedScriptFile() ?: throw RuntimeConfigurationError("Script path is not specified.")
        if (!file.extension.equals("pvm", ignoreCase = true)) {
            throw RuntimeConfigurationError("PaniniVM run configurations require a .pvm script.")
        }
        if (!file.exists()) throw RuntimeConfigurationError("Script file does not exist: ${file.path}")
        if (!file.isFile) throw RuntimeConfigurationError("Script path is not a file: ${file.path}")
    }

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState {
        return RunProfileState { _, _ ->
            val project = environment.project
            val consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(project).console

            val processHandler = PvmProcessHandler()
            consoleView.attachToProcess(processHandler)
            processHandler.startNotify()

            ApplicationManager.getApplication().executeOnPooledThread {
                if (processHandler.cancellationRequested) return@executeOnPooledThread
                val file = resolvedScriptFile()
                if (file == null) {
                    processHandler.notifyTextAvailable("Error: Script path is not specified\n", ProcessOutputTypes.STDERR)
                    processHandler.terminate(1)
                    return@executeOnPooledThread
                }
                if (!file.isFile || !file.extension.equals("pvm", ignoreCase = true)) {
                    processHandler.notifyTextAvailable("Error: Invalid PaniniVM script file: ${file.path}\n", ProcessOutputTypes.STDERR)
                    processHandler.terminate(1)
                    return@executeOnPooledThread
                }

                if (runViaVm) {
                    try {
                        val exitCode = PvmRunExecutor().execute(
                            file = file,
                            input = processHandler.input,
                            output = processHandler.consoleStream(ProcessOutputTypes.STDOUT),
                        )
                        processHandler.terminate(
                            if (processHandler.cancellationRequested) PvmProcessHandler.CANCELLED_EXIT_CODE
                            else exitCode,
                        )
                    } catch (failure: Throwable) {
                        if (!processHandler.cancellationRequested) {
                            processHandler.notifyTextAvailable(
                                "Error during execution: ${failure.message ?: failure::class.java.simpleName}\n",
                                ProcessOutputTypes.STDERR,
                            )
                        }
                        processHandler.terminate(
                            if (processHandler.cancellationRequested) PvmProcessHandler.CANCELLED_EXIT_CODE else 1,
                        )
                    }
                } else {
                    val basePath = project.basePath ?: ""

                    try {
                        processHandler.notifyTextAvailable("=== PaniniVM Script Execution (CLI Process): ${file.name} ===\n", ProcessOutputTypes.STDOUT)
                        val workingDirectory = basePath.takeIf(String::isNotBlank)?.let(::File)
                            ?.takeIf(File::isDirectory) ?: file.parentFile ?: File(".").absoluteFile
                        val process = PvmCliProcessLauncher().start(file, workingDirectory)
                        if (!processHandler.bind(process)) return@executeOnPooledThread

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
