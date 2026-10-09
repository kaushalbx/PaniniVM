package dev.panini.cli

import java.io.File
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit
import java.util.concurrent.Executors
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode

// Each method launches a fresh JVM; avoid overlapping these startup-sensitive
// checks while retaining the same deadline for every individual process.
@Execution(ExecutionMode.SAME_THREAD)
class PaniniCliProcessTest {
    @Test
    fun `launcher reads two values and exits successfully`() {
        val result = runCli("10\n20\n")

        assertEquals(0, result.exitCode)
        assertTrue(result.output.contains("Enter value for प्रथम (number):"))
        assertTrue(result.output.contains("Enter value for द्वितीय (number):"))
        assertTrue(result.output.contains("त्रिंशत्"))
        assertFalse(result.output.contains("Exception"))
    }

    @Test
    fun `launcher cancellation exits unsuccessfully without a stack trace`() {
        val result = runCli("10\n:cancel\n")

        assertEquals(1, result.exitCode)
        assertTrue(result.output.contains("Execution cancelled while reading द्वितीय."))
        assertFalse(result.output.contains("Exception"))
        assertFalse(result.output.contains("at dev.panini"))
    }

    @Test
    fun `launcher end of input exits unsuccessfully without a stack trace`() {
        val result = runCli("10\n")

        assertEquals(1, result.exitCode)
        assertTrue(result.output.contains("end of input while reading द्वितीय"))
        assertFalse(result.output.contains("Exception"))
        assertFalse(result.output.contains("at dev.panini"))
    }

    @Test
    fun `launcher validates typed boolean and choice input`() {
        val result = runCli(
            input = "अर्जुन\nunknown\nआम्\nहरित\nनील\n",
            scriptPath = "cli/examples/interactive_typed_input.pvm",
        )

        assertEquals(0, result.exitCode)
        assertTrue(result.output.contains("Invalid boolean 'unknown'."))
        assertTrue(result.output.contains("Enter value for वर्ण (लोहित/नील):"))
        assertTrue(result.output.contains("Invalid choice 'हरित'."))
        assertTrue(result.output.contains("नील"))
    }

    private fun runCli(
        input: String,
        scriptPath: String = "cli/examples/interactive_addition.pvm",
    ): ProcessResult {
        val javaExecutable = File(System.getProperty("java.home"), "bin/java").absolutePath
        val classpath = requireNotNull(System.getProperty("panini.cli.test.classpath"))
        val script = File(scriptPath).absoluteFile
        val process = ProcessBuilder(
            javaExecutable,
            "-Dfile.encoding=UTF-8",
            "-cp",
            classpath,
            "dev.panini.MainKt",
            "--eval",
            script.absolutePath,
        )
            .directory(File(System.getProperty("user.dir")))
            .redirectErrorStream(true)
            .start()

        // Drain output while the child runs: waiting before reading can fill
        // the OS pipe and block the child, disguising an output deadlock as slow startup.
        val outputReader = Executors.newSingleThreadExecutor { task ->
            Thread(task, "panini-cli-test-output").apply { isDaemon = true }
        }
        val output = outputReader.submit<String> {
            process.inputStream.use { it.readBytes().toString(StandardCharsets.UTF_8) }
        }

        return try {
            process.outputStream.use { stream ->
                stream.write(input.toByteArray(StandardCharsets.UTF_8))
            }
            val finished = process.waitFor(30, TimeUnit.SECONDS)
            if (!finished) {
                process.destroyForcibly()
                process.waitFor(5, TimeUnit.SECONDS)
            }
            val capturedOutput = output.get(5, TimeUnit.SECONDS)
            assertTrue(finished, "CLI process did not finish within 30 seconds. Output:\n$capturedOutput")
            ProcessResult(
                exitCode = process.exitValue(),
                output = capturedOutput,
            )
        } finally {
            if (process.isAlive) process.destroyForcibly()
            outputReader.shutdownNow()
        }
    }

    private data class ProcessResult(
        val exitCode: Int,
        val output: String,
    )
}
