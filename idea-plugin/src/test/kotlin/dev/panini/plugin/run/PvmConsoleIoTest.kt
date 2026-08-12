package dev.panini.plugin.run

import java.nio.charset.StandardCharsets
import kotlin.test.Test
import kotlin.test.assertEquals

class PvmConsoleIoTest {
    @Test
    fun `console input is available to the running PVM`() {
        val io = PvmConsoleIo()

        io.inputSink.write("१०\n".toByteArray(StandardCharsets.UTF_8))
        io.inputSink.flush()

        assertEquals("१०", io.input.bufferedReader(StandardCharsets.UTF_8).readLine())
        io.close()
    }

    @Test
    fun `output preserves UTF-8 characters split across writes`() {
        val rendered = mutableListOf<String>()
        val output = PvmConsoleIo().output(rendered::add)
        val bytes = "Enter प्रथम:\n".toByteArray(StandardCharsets.UTF_8)

        bytes.forEach { output.write(it.toInt()) }

        assertEquals(listOf("Enter प्रथम:\n"), rendered)
    }

    @Test
    fun `complete lines are emitted while partial lines remain buffered`() {
        val rendered = mutableListOf<String>()
        val output = PvmConsoleIo().output(rendered::add)

        output.write("first\nsecond".toByteArray(StandardCharsets.UTF_8))
        assertEquals(listOf("first\n"), rendered)

        output.write(" line\n".toByteArray(StandardCharsets.UTF_8))
        assertEquals(listOf("first\n", "second line\n"), rendered)
    }

    @Test
    fun `closing output flushes a prompt without newline`() {
        val rendered = mutableListOf<String>()
        val output = PvmConsoleIo().output(rendered::add)

        output.write("Allow execution? [y/N]:".toByteArray(StandardCharsets.UTF_8))
        output.close()

        assertEquals(listOf("Allow execution? [y/N]:"), rendered)
    }
}
