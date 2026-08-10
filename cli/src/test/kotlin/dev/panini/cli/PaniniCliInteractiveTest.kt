package dev.panini.cli

import dev.panini.execution.ExecutionResult
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class PaniniCliInteractiveTest {
    @Test
    fun `script read operation consumes a value from stdin`() {
        val directory = Files.createTempDirectory("interactive-read")
        val script = directory.resolve("read.pvm")
        val output = ByteArrayOutputStream()
        try {
            Files.writeString(script, "आगत + अम् ग्रह् + लोट् + सिप् ।")
            val cli = PaniniCli(
                inputStream = ByteArrayInputStream("दश\n".toByteArray(Charsets.UTF_8)),
                outputStream = PrintStream(output, true, Charsets.UTF_8),
            )

            val result = assertIs<ExecutionResult.Success>(cli.executeScriptFile(script.toFile()).single())

            assertEquals("दश", result.value)
            assertTrue(output.toString(Charsets.UTF_8).contains("Enter value for आगत:"))
        } finally {
            Files.deleteIfExists(script)
            Files.deleteIfExists(directory)
        }
    }

    @Test
    fun `interactive addition script reads two numeric values and prints their sum`() {
        val output = ByteArrayOutputStream()
        val cli = PaniniCli(
            inputStream = ByteArrayInputStream("10\n२०\n".toByteArray(Charsets.UTF_8)),
            outputStream = PrintStream(output, true, Charsets.UTF_8),
        )

        val results = cli.executeScriptFile(File("cli/examples/interactive_addition.pvm"))
        val successes = results.filterIsInstance<ExecutionResult.Success>()

        assertEquals("त्रिंशत्", successes.last().value)
        val rendered = output.toString(Charsets.UTF_8)
        assertTrue(rendered.contains("Enter value for प्रथम:"))
        assertTrue(rendered.contains("Enter value for द्वितीय:"))
        assertTrue(rendered.contains("त्रिंशत्"))
        assertTrue(!rendered.contains("Line 1:"))
        assertTrue(!rendered.contains("Line 2:"))
        assertTrue(!rendered.contains("Line 3:"))
    }
}
