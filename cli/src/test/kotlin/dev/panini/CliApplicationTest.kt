package dev.panini

import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CliApplicationTest {
    @Test
    fun `no arguments starts the repl`() {
        var started = false
        val application = CliApplication(replRunner = { started = true })

        assertEquals(0, application.run(emptyArray()))
        assertTrue(started)
    }

    @Test
    fun `regular command output is rendered by the application`() {
        val bytes = ByteArrayOutputStream()
        val application = CliApplication(
            output = PrintStream(bytes, true, Charsets.UTF_8),
            commandRunner = { listOf("first", "second") },
        )

        assertEquals(0, application.run(arrayOf("--example")))
        assertEquals("first\nsecond\n", bytes.toString(Charsets.UTF_8).replace("\r\n", "\n"))
    }

    @Test
    fun `script mode maps runtime failures to exit code one`() {
        val application = CliApplication(
            scriptRunner = {
                listOf(ExecutionResult.Failure(ExecutionError.ACTION_FAILED, "failed"))
            },
        )

        assertEquals(1, application.run(arrayOf("--eval", "program.pvm")))
    }

    @Test
    fun `script aliases share successful execution path`() {
        val visited = mutableListOf<String>()
        val application = CliApplication(
            scriptRunner = {
                visited += it.path
                emptyList()
            },
        )

        listOf("--eval", "--pvm", "--exec").forEach { command ->
            assertEquals(0, application.run(arrayOf(command, "program.pvm")))
        }
        assertEquals(listOf("program.pvm", "program.pvm", "program.pvm"), visited)
    }
}
