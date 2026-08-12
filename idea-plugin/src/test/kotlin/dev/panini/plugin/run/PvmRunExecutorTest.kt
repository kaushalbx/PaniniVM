package dev.panini.plugin.run

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PvmRunExecutorTest {
    @Test
    fun `interactive addition succeeds with typed IDE input`() {
        val result = execute("10\n२०\n")

        assertEquals(0, result.exitCode)
        assertTrue(result.output.contains("Enter value for प्रथम (number):"))
        assertTrue(result.output.contains("Enter value for द्वितीय (number):"))
        assertTrue(result.output.contains("त्रिंशत्"))
    }

    @Test
    fun `cancel produces failure exit without stack trace`() {
        val result = execute("10\n:cancel\n")

        assertEquals(1, result.exitCode)
        assertTrue(result.output.contains("Execution cancelled while reading द्वितीय."))
        assertFalse(result.output.contains("Exception"))
        assertFalse(result.output.contains("at dev.panini"))
    }

    @Test
    fun `end of input produces failure exit`() {
        val result = execute("10\n")

        assertEquals(1, result.exitCode)
        assertTrue(result.output.contains("end of input while reading द्वितीय"))
    }

    @Test
    fun `closing IDE input unblocks a script waiting at a prompt`() {
        val io = PvmConsoleIo()
        val output = ByteArrayOutputStream()
        val pool = Executors.newSingleThreadExecutor()
        try {
            val execution = pool.submit<Int> {
                PvmRunExecutor().execute(script, io.input, PrintStream(output, true, Charsets.UTF_8))
            }

            waitUntil { output.toString(Charsets.UTF_8).contains("Enter value for प्रथम") }
            io.close()

            assertEquals(1, execution.get(10, TimeUnit.SECONDS))
            assertTrue(output.toString(Charsets.UTF_8).contains("end of input"))
        } finally {
            io.close()
            pool.shutdownNow()
        }
    }

    private fun execute(input: String): RunResult {
        val output = ByteArrayOutputStream()
        val exitCode = PvmRunExecutor().execute(
            script,
            ByteArrayInputStream(input.toByteArray(Charsets.UTF_8)),
            PrintStream(output, true, Charsets.UTF_8),
        )
        return RunResult(exitCode, output.toString(Charsets.UTF_8))
    }

    private fun waitUntil(condition: () -> Boolean) {
        val deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(10)
        while (!condition()) {
            check(System.nanoTime() < deadline) { "Timed out waiting for the input prompt." }
            Thread.yield()
        }
    }

    private data class RunResult(val exitCode: Int, val output: String)

    private companion object {
        val script = File("cli/examples/interactive_addition.pvm")
    }
}
