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
import dev.panini.execution.ExecutionResult
import dev.panini.execution.PvmScript
import dev.panini.execution.VM
import com.intellij.execution.configurations.RunConfigurationOptions
import java.io.File
import java.io.OutputStream

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

                if (runViaVm) {
                    try {
                        processHandler.notifyTextAvailable("=== PaniniVM Script Execution (Direct VM): ${file.name} ===\n", ProcessOutputTypes.STDOUT)
                        val statements = PvmScript.parse(file.readText())
                        val sessionKey = "session_${file.nameWithoutExtension}_${System.currentTimeMillis()}"

                         statements.forEachIndexed { index, statement ->
                            val res = VM.eval(statement.text, sessionKey = sessionKey)
                            when (res) {
                                is ExecutionResult.Success -> {
                                    if (res.value.isNotBlank() && isPrintResult(res, statement.text)) {
                                        processHandler.notifyTextAvailable("${res.value}\n", ProcessOutputTypes.STDOUT)
                                    }
                                }
                                is ExecutionResult.Failure -> {
                                    processHandler.notifyTextAvailable("Error: ${res.message}\n", ProcessOutputTypes.STDERR)
                                }
                                is ExecutionResult.NeedsInput -> {
                                    processHandler.notifyTextAvailable("Needs input: ${res.message} (missing: ${res.missingKarakas})\n", ProcessOutputTypes.STDOUT)
                                }
                                is ExecutionResult.Ambiguous -> {
                                    processHandler.notifyTextAvailable("Ambiguous: ${res.message} (matches: ${res.matchingOperations})\n", ProcessOutputTypes.STDOUT)
                                }
                                is ExecutionResult.NeedsApproval -> {
                                    processHandler.notifyTextAvailable("Needs approval: ID: ${res.invocationId} (effects: ${res.requiredEffects})\n", ProcessOutputTypes.STDOUT)
                                }
                                is ExecutionResult.NeedsAcceptance -> {
                                    processHandler.notifyTextAvailable("Needs acceptance: ID: ${res.invocationId} (from ${res.speaker} to ${res.listener})\n", ProcessOutputTypes.STDOUT)
                                }
                            }
                        }
                        processHandler.terminate(0)
                    } catch (e: Throwable) {
                        val stackTrace = java.io.StringWriter().also { e.printStackTrace(java.io.PrintWriter(it)) }.toString()
                        processHandler.notifyTextAvailable("Error during execution:\n$stackTrace\n", ProcessOutputTypes.STDERR)
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

    private fun isPrintResult(res: ExecutionResult.Success, statementText: String): Boolean {
        val trimmed = statementText.trim()
        if (trimmed.contains("मुद्र्") || trimmed.contains("दृश्") || trimmed.contains("प्रेष्")) return true
        return res.trace.any {
            it.contains("Printed") || it.contains("प्रदर्शनम्") || it.contains("मुद्रणम्") || it.contains("प्रेषणम्")
        }
    }
}
