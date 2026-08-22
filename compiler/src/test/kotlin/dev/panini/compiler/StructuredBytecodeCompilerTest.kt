package dev.panini.compiler

import dev.panini.execution.SanskritValue
import dev.panini.execution.ExecutionResult
import dev.panini.execution.PaniniVM
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.ASM9
import org.objectweb.asm.Opcodes.GOTO
import org.objectweb.asm.Opcodes.IFEQ
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StructuredBytecodeCompilerTest {
    @Test
    fun `parameterized named operation has interpreter compiler parity`() {
        val source = """
            व्यवकलन + ल्युट् + सुँ ।
            वाम + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            दक्षिण + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            सङ्ख्या + सुँ इति परिणाम + सुँ ।
            वाम + अम् दक्षिण + अम् च वि + युज् + णिच् + लोट् + सिप् ॥

            दक्षिण + ङस् द्वि + अम् वाम + ङस् पञ्च + अम् व्यवकलन + ल्युट् + टा कृ + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
            .filterIsInstance<ExecutionResult.Success>().last().typedValue
        val generated = BytecodeCompiler.compileAndLoad(source, "CompiledParameterizedSamjna")
        @Suppress("UNCHECKED_CAST")
        val compiled = (generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>)
            .getValue("LastResult")

        assertEquals(interpreted, compiled)
        assertEquals(3L, (compiled as SanskritValue.Sankhya).value)
    }

    @Test
    fun `two-counter proof compiles to JVM branches and executes`() {
        val source = File("examples/control_flow/two_counter_machine.pvm").readText()
        val bytes = BytecodeCompiler.compile(source, "CompiledTwoCounterMachine")
        val jumps = mutableListOf<Int>()
        ClassReader(bytes).accept(
            object : ClassVisitor(ASM9) {
                override fun visitMethod(
                    access: Int,
                    name: String?,
                    descriptor: String?,
                    signature: String?,
                    exceptions: Array<out String>?,
                ): MethodVisitor = object : MethodVisitor(ASM9) {
                    override fun visitJumpInsn(opcode: Int, label: Label?) {
                        jumps += opcode
                    }
                }
            },
            0,
        )

        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes("CompiledTwoCounterMachine", bytes)
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>

        assertTrue(IFEQ in jumps, "Conditional and loop exits must use JVM conditional branches.")
        assertTrue(GOTO in jumps, "The unbounded loop must contain a JVM backward branch.")
        assertEquals("त्रीणि", values.getValue("LastResult").toDisplayText())
    }
}
