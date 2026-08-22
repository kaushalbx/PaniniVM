package dev.panini.compiler

import org.objectweb.asm.ClassReader
import org.objectweb.asm.util.CheckClassAdapter
import java.io.PrintWriter
import java.io.StringWriter

/** Rejects malformed generated classes before they can be written or loaded. */
internal object GeneratedBytecodeVerifier {
    fun verify(bytes: ByteArray): ByteArray {
        val diagnostics = StringWriter()
        CheckClassAdapter.verify(ClassReader(bytes), false, PrintWriter(diagnostics))
        require(diagnostics.toString().isBlank()) {
            "Generated JVM bytecode failed ASM verification:\n$diagnostics"
        }
        return bytes
    }
}
