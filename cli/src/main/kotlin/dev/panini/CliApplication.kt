package dev.panini

import dev.panini.cli.PaniniCli
import dev.panini.execution.ExecutionResult
import java.io.File
import java.io.PrintStream

/** Selects the CLI execution mode without owning JVM process lifecycle concerns. */
internal class CliApplication(
    private val output: PrintStream = System.out,
    private val replRunner: () -> Unit = { PaniniCli(outputStream = output).startRepl() },
    private val scriptRunner: (File) -> List<ExecutionResult> = {
        PaniniCli(outputStream = output).executeScriptFile(it)
    },
    private val commandRunner: (Array<String>) -> List<String> = ::runCli,
) {
    fun run(args: Array<String>): Int = when {
        args.isEmpty() -> {
            replRunner()
            SUCCESS
        }
        args.first() in scriptCommands -> executeScript(args)
        else -> {
            commandRunner(args).forEach(output::println)
            SUCCESS
        }
    }

    private fun executeScript(args: Array<String>): Int {
        val filePath = args.getOrNull(1) ?: error("Usage: --eval path/to/file.pvm")
        val results = scriptRunner(File(filePath))
        return if (results.any { it is ExecutionResult.Failure }) FAILURE else SUCCESS
    }

    private companion object {
        const val SUCCESS = 0
        const val FAILURE = 1
        val scriptCommands = setOf("--eval", "--pvm", "--exec")
    }
}
