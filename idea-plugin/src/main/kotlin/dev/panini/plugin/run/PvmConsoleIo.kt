package dev.panini.plugin.run

import java.io.ByteArrayOutputStream
import java.io.Closeable
import java.io.OutputStream
import java.io.PipedInputStream
import java.io.PipedOutputStream
import java.nio.charset.StandardCharsets

/** Bidirectional UTF-8 bridge between an IntelliJ Run console and a PVM execution. */
internal class PvmConsoleIo : Closeable {
    val input = PipedInputStream()
    val inputSink: OutputStream = PipedOutputStream(input)

    fun output(onText: (String) -> Unit): OutputStream = LineOutputStream(onText)

    override fun close() {
        inputSink.close()
    }

    private class LineOutputStream(
        private val onText: (String) -> Unit,
    ) : OutputStream() {
        private val pending = ByteArrayOutputStream()

        @Synchronized
        override fun write(value: Int) {
            pending.write(value)
            if (value == '\n'.code) flush()
        }

        @Synchronized
        override fun write(bytes: ByteArray, offset: Int, length: Int) {
            pending.write(bytes, offset, length)
            if (bytes.copyOfRange(offset, offset + length).contains('\n'.code.toByte())) flushCompleteLines()
        }

        @Synchronized
        override fun flush() {
            emit(pending.size())
        }

        @Synchronized
        override fun close() {
            flush()
        }

        private fun flushCompleteLines() {
            val bytes = pending.toByteArray()
            val lastNewline = bytes.indexOfLast { it == '\n'.code.toByte() }
            if (lastNewline < 0) return
            emit(lastNewline + 1)
        }

        private fun emit(length: Int) {
            if (length == 0) return
            val bytes = pending.toByteArray()
            onText(String(bytes, 0, length, StandardCharsets.UTF_8))
            pending.reset()
            if (length < bytes.size) pending.write(bytes, length, bytes.size - length)
        }
    }
}
