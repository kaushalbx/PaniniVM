package dev.panini.cli

import dev.panini.execution.ExecutionResult
import dev.panini.execution.OutputKind
import dev.panini.execution.PaniniVM
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
            inputStream = ByteArrayInputStream("not-a-number\n10\n२०\n".toByteArray(Charsets.UTF_8)),
            outputStream = PrintStream(output, true, Charsets.UTF_8),
        )

        val results = cli.executeScriptFile(File("cli/examples/interactive_addition.pvm"))
        val successes = results.filterIsInstance<ExecutionResult.Success>()

        assertEquals("त्रिंशत्", successes.last().value)
        assertEquals(OutputKind.INTERNAL, successes.first().outputKind)
        assertEquals(OutputKind.CONSOLE, successes.last().outputKind)
        val rendered = output.toString(Charsets.UTF_8)
        assertTrue(rendered.contains("Enter value for प्रथम (number):"))
        assertTrue(rendered.contains("Invalid number 'not-a-number'."))
        assertTrue(rendered.contains("Enter value for द्वितीय (number):"))
        assertTrue(rendered.contains("त्रिंशत्"))
        assertTrue(!rendered.contains("Line 1:"))
        assertTrue(!rendered.contains("Line 2:"))
        assertTrue(!rendered.contains("Line 3:"))
    }

    @Test
    fun `typed input script validates boolean and choice values`() {
        val output = ByteArrayOutputStream()
        val cli = PaniniCli(
            inputStream = ByteArrayInputStream("अर्जुन\nperhaps\nआम्\nहरित\nनील\n".toByteArray(Charsets.UTF_8)),
            outputStream = PrintStream(output, true, Charsets.UTF_8),
        )

        val results = cli.executeScriptFile(File("cli/examples/interactive_typed_input.pvm"))

        assertTrue(results.none { it is ExecutionResult.Failure })
        assertEquals("नील", assertIs<ExecutionResult.Success>(results.last()).value)
        val rendered = output.toString(Charsets.UTF_8)
        assertTrue(rendered.contains("Enter value for नाम:"))
        assertTrue(rendered.contains("Enter value for अनुमत (boolean):"))
        assertTrue(rendered.contains("Invalid boolean 'perhaps'."))
        assertTrue(rendered.contains("Enter value for वर्ण (लोहित/नील):"))
        assertTrue(rendered.contains("Invalid choice 'हरित'."))
        assertTrue(rendered.contains("नील"))
    }

    @Test
    fun `script approval prompt grants effects and resumes continuation`() {
        val output = ByteArrayOutputStream()
        val cli = PaniniCli(
            inputStream = ByteArrayInputStream("yes\n".toByteArray(Charsets.UTF_8)),
            outputStream = PrintStream(output, true, Charsets.UTF_8),
        )

        val results = cli.executeScriptFile(File("examples/external/external_demo.pvm"))

        assertTrue(results.none { it is ExecutionResult.NeedsApproval })
        assertEquals(OutputKind.EXTERNAL, assertIs<ExecutionResult.Success>(results.first()).outputKind)
        val rendered = output.toString(Charsets.UTF_8)
        assertTrue(rendered.contains("requires: NETWORK, EXECUTE_PROCESS, SEND_MESSAGE"))
        assertTrue(rendered.contains("Allow execution? [y/N]:"))
    }

    @Test
    fun `script approval rejection becomes a clean failure`() {
        val output = ByteArrayOutputStream()
        val cli = PaniniCli(
            inputStream = ByteArrayInputStream("no\n".toByteArray(Charsets.UTF_8)),
            outputStream = PrintStream(output, true, Charsets.UTF_8),
        )

        val results = cli.executeScriptFile(File("examples/external/external_demo.pvm"))

        val failure = assertIs<ExecutionResult.Failure>(results.first())
        assertTrue(failure.message.contains("denied by user"))
    }

    @Test
    fun `cancel during script input rolls back partial session state`() {
        val output = ByteArrayOutputStream()
        val vm = PaniniVM()
        val cli = PaniniCli(
            vm = vm,
            inputStream = ByteArrayInputStream("10\n:cancel\n".toByteArray(Charsets.UTF_8)),
            outputStream = PrintStream(output, true, Charsets.UTF_8),
        )

        val results = cli.executeScriptFile(File("cli/examples/interactive_addition.pvm"))

        val failure = assertIs<ExecutionResult.Failure>(results.single())
        assertTrue(failure.message.contains("Execution cancelled"))
        assertTrue(vm.listSessions().isEmpty())
        assertTrue(!output.toString(Charsets.UTF_8).contains("Exception"))
    }

    @Test
    fun `end of input stops script without a stack trace`() {
        val output = ByteArrayOutputStream()
        val vm = PaniniVM()
        val cli = PaniniCli(
            vm = vm,
            inputStream = ByteArrayInputStream("10\n".toByteArray(Charsets.UTF_8)),
            outputStream = PrintStream(output, true, Charsets.UTF_8),
        )

        val results = cli.executeScriptFile(File("cli/examples/interactive_addition.pvm"))

        val failure = assertIs<ExecutionResult.Failure>(results.single())
        assertTrue(failure.message.contains("end of input"))
        assertTrue(vm.listSessions().isEmpty())
        assertTrue(!output.toString(Charsets.UTF_8).contains("Exception"))
    }
}
