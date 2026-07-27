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
import dev.panini.main
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.OutputStream
import java.io.PrintStream

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

                processHandler.notifyTextAvailable("Executing PaniniVM script: $path\n", ProcessOutputTypes.STDOUT)

                val oldOut = System.out
                val oldErr = System.err
                val baos = ByteArrayOutputStream()
                val ps = PrintStream(baos)
                try {
                    System.setOut(ps)
                    System.setErr(ps)

                    main(arrayOf("--eval", path))

                    val outputText = baos.toString("UTF-8")
                    processHandler.notifyTextAvailable(outputText, ProcessOutputTypes.STDOUT)
                    processHandler.terminate(0)
                } catch (e: Throwable) {
                    val stackTrace = java.io.StringWriter().also { e.printStackTrace(java.io.PrintWriter(it)) }.toString()
                    processHandler.notifyTextAvailable("Error during execution:\n$stackTrace\n", ProcessOutputTypes.STDERR)
                    processHandler.terminate(1)
                } finally {
                    System.setOut(oldOut)
                    System.setErr(oldErr)
                }
            }

            DefaultExecutionResult(consoleView, processHandler)
        }
    }
}
