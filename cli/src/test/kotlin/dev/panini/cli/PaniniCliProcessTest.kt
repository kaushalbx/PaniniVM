package dev.panini.cli

import java.io.File
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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

    private fun runCli(input: String): ProcessResult {
        val javaExecutable = File(System.getProperty("java.home"), "bin/java").absolutePath
        val classpath = requireNotNull(System.getProperty("panini.cli.test.classpath"))
        val script = File("cli/examples/interactive_addition.pvm").absoluteFile
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

        return try {
            process.outputStream.use { stream ->
                stream.write(input.toByteArray(StandardCharsets.UTF_8))
            }
            assertTrue(process.waitFor(30, TimeUnit.SECONDS), "CLI process did not finish within 30 seconds")
            ProcessResult(
                exitCode = process.exitValue(),
                output = process.inputStream.readBytes().toString(StandardCharsets.UTF_8),
            )
        } finally {
            if (process.isAlive) process.destroyForcibly()
        }
    }

    private data class ProcessResult(
        val exitCode: Int,
        val output: String,
    )
}
