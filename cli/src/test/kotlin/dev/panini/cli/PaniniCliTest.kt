package dev.panini.cli

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PaniniCliTest {

    @Test
    fun `PaniniCli processes REPL commands correctly`() {
        val input = """
            :help
            :dhatu 07.0007
            :trace
            एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।
            :compile examples/linguistic/sandhi.pvm SandhiCliTest
            :exit
        """.trimIndent()

        val inputStream = ByteArrayInputStream(input.toByteArray())
        val outputStream = ByteArrayOutputStream()
        val printStream = PrintStream(outputStream)

        val cli = PaniniCli(inputStream = inputStream, outputStream = printStream)
        cli.startRepl()

        val output = outputStream.toString()
        assertTrue(output.contains("PāṇiniVM Interactive REPL"))
        assertTrue(output.contains("Available REPL Commands"))
        assertTrue(output.contains("Dhātu 07.0007"))
        assertTrue(output.contains("Derivation trace log: ENABLED"))
        assertTrue(output.contains("⇒ त्रीणि"))
        assertTrue(output.contains("✓ Compiled sandhi.pvm"))
        assertTrue(output.contains("शुभमस्तु!"))
    }

    @Test
    fun `PaniniCli executes script file from CLI args`() {
        val outputStream = ByteArrayOutputStream()
        val printStream = PrintStream(outputStream)

        val cli = PaniniCli(outputStream = printStream)
        val file = File("examples/linguistic/sandhi.pvm")
        val results = cli.executeScriptFile(file)

        assertEquals(3, results.size)
        val output = outputStream.toString()
        assertTrue(output.contains("Line 1: रामावतार"), output)
        assertTrue(output.contains("Line 2: देवालय"), output)
        assertTrue(output.contains("Line 3: तच्छिव"), output)
    }
}
