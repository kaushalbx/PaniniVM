package dev.panini.plugin.run

import dev.panini.cli.PaniniCli
import dev.panini.execution.ExecutionResult
import java.io.File
import java.io.InputStream
import java.io.PrintStream

/** Executes one PVM file for an IDE Run configuration and maps its result to a process exit code. */
internal class PvmRunExecutor(
    private val cliFactory: (InputStream, PrintStream) -> PaniniCli = { input, output ->
        PaniniCli(inputStream = input, outputStream = output)
    },
) {
    fun execute(file: File, input: InputStream, output: PrintStream): Int = try {
        val results = cliFactory(input, output).executeScriptFile(file)
        if (results.any { it is ExecutionResult.Failure }) FAILURE else SUCCESS
    } catch (error: Throwable) {
        output.println("Error during execution: ${error.message ?: error::class.simpleName}")
        FAILURE
    }

    private companion object {
        const val SUCCESS = 0
        const val FAILURE = 1
    }
}
