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
import org.objectweb.asm.Opcodes.LCMP
import org.objectweb.asm.Opcodes.LLOAD
import java.io.File
import java.lang.reflect.InvocationTargetException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFailsWith

class StructuredBytecodeCompilerTest {
    @Test
    fun `large grammatical loop bounds use JVM long counters`() {
        val source = """
            शून्य + अम् अवस्था + ङे दा + लोट् + सिप् ।
            कोटि + कृत्वः यावत् अवस्था + अम् शून्य + अम् च विद् + लोट् + सिप् तावत् एक + अम् अवस्था + ङे दा + लोट् + सिप् ।
        """.trimIndent()
        val bytes = BytecodeCompiler.compile(source, "CompiledLargeLoopBound")
        val instructions = mutableListOf<Int>()
        ClassReader(bytes).accept(
            object : ClassVisitor(ASM9) {
                override fun visitMethod(
                    access: Int,
                    name: String?,
                    descriptor: String?,
                    signature: String?,
                    exceptions: Array<out String>?,
                ): MethodVisitor = object : MethodVisitor(ASM9) {
                    override fun visitInsn(opcode: Int) {
                        instructions += opcode
                    }

                    override fun visitVarInsn(opcode: Int, varIndex: Int) {
                        instructions += opcode
                    }
                }
            },
            0,
        )
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes("CompiledLargeLoopBound", bytes)
        @Suppress("UNCHECKED_CAST")
        val result = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
        val outcome = result.getValue("परिणाम") as SanskritValue.Rupa

        assertTrue(LLOAD in instructions)
        assertTrue(LCMP in instructions)
        assertEquals(0L, (outcome.fields.getValue("प्रयत्नसङ्ख्या") as SanskritValue.Sankhya).value)
    }

    @Test
    fun `nested compiled loops preserve interpreter state parity`() {
        val source = """
            हृ + ल्युट् + सुँ ।
            अन्तरावस्था + अम् एक + अम् च वि + युज् + णिच् + लोट् + सिप् ततः दा + लोट् + सिप् फल + अम् अन्तरावस्था + ङे ॥

            क्षि + ल्युट् + सुँ ।
            बाह्यावस्था + अम् एक + अम् च वि + युज् + णिच् + लोट् + सिप् ततः दा + लोट् + सिप् फल + अम् बाह्यावस्था + ङे ॥

            अन्तरचक्र + ल्युट् + सुँ ।
            द्वि + अम् अन्तरावस्था + ङे दा + लोट् + सिप् ।
            यावत् अन्तरावस्था + अम् शून्य + अम् च विद् + लोट् + सिप् तावत् हृ + ल्युट् + टा कृ + लोट् + सिप् ॥

            बाह्यचक्र + ल्युट् + सुँ ।
            अन्तरचक्र + ल्युट् + टा कृ + लोट् + सिप् ।
            क्षि + ल्युट् + टा कृ + लोट् + सिप् ॥

            द्वि + अम् बाह्यावस्था + ङे दा + लोट् + सिप् ।
            यावत् बाह्यावस्था + अम् शून्य + अम् च विद् + लोट् + सिप् तावत् बाह्यचक्र + ल्युट् + टा कृ + लोट् + सिप् ।
            मुद्र् + णिच् + लोट् + सिप् बाह्यावस्था + अम् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
            .filterIsInstance<ExecutionResult.Success>().last().typedValue
        val generated = BytecodeCompiler.compileAndLoad(source, "CompiledNestedLoops")
        @Suppress("UNCHECKED_CAST")
        val compiled = (generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>)
            .getValue("LastResult")

        assertEquals(interpreted, compiled)
        assertEquals("शून्यम्", compiled.toDisplayText())
    }

    @Test
    fun `recursive generated samjna calls unwind and resume top-level execution`() {
        val source = """
            हृ + ल्युट् + सुँ ।
            अवस्था + अम् एक + अम् च वि + युज् + णिच् + लोट् + सिप् ततः दा + लोट् + सिप् फल + अम् अवस्था + ङे ।
            गण् + ल्युट् + टा कृ + लोट् + सिप् ॥

            गण् + ल्युट् + सुँ ।
            यदि अवस्था + अम् शून्य + अम् च विद् + लोट् + सिप् तर्हि हृ + ल्युट् + टा कृ + लोट् + सिप् अन्यथा वि + स्था + लोट् + सिप् ॥

            त्रि + अम् अवस्था + ङे दा + लोट् + सिप् ।
            गण् + ल्युट् + टा कृ + लोट् + सिप् ।
            मुद्र् + णिच् + लोट् + सिप् अवस्था + अम् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
            .filterIsInstance<ExecutionResult.Success>().last().typedValue
        val generated = BytecodeCompiler.compileAndLoad(source, "CompiledRecursiveCountdown")
        @Suppress("UNCHECKED_CAST")
        val compiled = (generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>)
            .getValue("LastResult")

        assertEquals(interpreted, compiled)
        assertEquals("शून्यम्", compiled.toDisplayText())
    }

    @Test
    fun `compiled named calls enforce signatures and prohibitions`() {
        val wrongType = """
            द्विगुणन + ल्युट् + सुँ ।
            मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            सङ्ख्या + सुँ इति परिणाम + सुँ ।
            मान + अम् द्वि + अम् च गुण् + णिच् + लोट् + सिप् ॥
            राम + अम् द्विगुणन + ल्युट् + टा कृ + लोट् + सिप् ।
        """.trimIndent()
        val prohibited = """
            विभाज् + ल्युट् + सुँ ।
            न द्वितीय + अम् शून्य + अम् ।
            प्रथम + अम् द्वितीय + अम् च भाज् + णिच् + लोट् + सिप् ॥
            दश + अम् शून्य + अम् च विभाज् + ल्युट् + टा कृ + लोट् + सिप् ।
        """.trimIndent()

        val typeFailure = assertFailsWith<IllegalArgumentException> {
            BytecodeCompiler.compile(wrongType, "RejectedCompiledType")
        }
        val prohibitionFailure = assertFailsWith<IllegalArgumentException> {
            BytecodeCompiler.compile(prohibited, "RejectedCompiledProhibition")
        }

        assertTrue(typeFailure.message.orEmpty().contains("मानप्रकार"))
        assertTrue(prohibitionFailure.message.orEmpty().contains("निषेध-प्रतिषेध"))
    }

    @Test
    fun `phala controlled loops have interpreter compiler parity`() {
        fun execute(source: String, className: String): Pair<SanskritValue, SanskritValue> {
            val interpreted = PaniniVM().evalScript(source)
                .filterIsInstance<ExecutionResult.Success>().last().typedValue!!
            val generated = BytecodeCompiler.compileAndLoad(source, className)
            @Suppress("UNCHECKED_CAST")
            val compiled = (generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>)
                .getValue("LastResult")
            return interpreted to compiled
        }

        val exhausted = """
            प्रयत्न + ल्युट् + सुँ ।
            एक + अम् द्वि + अम् च विद् + लोट् + सिप् ॥
            द्वि + कृत्वः यावत् फल + सुँ न तावत् प्रयत्न + ल्युट् + टा कृ + लोट् + सिप् ।
        """.trimIndent()
        val victory = """
            प्रयत्न + ल्युट् + सुँ ।
            द्वि + अम् एक + अम् च विद् + लोट् + सिप् ॥
            पञ्च + कृत्वः यावत् फल + सुँ न तावत् प्रयत्न + ल्युट् + टा कृ + लोट् + सिप् ।
        """.trimIndent()

        val exhaustedResults = execute(exhausted, "CompiledPhalaExhaustion")
        val victoryResults = execute(victory, "CompiledPhalaVictory")
        assertEquals(exhaustedResults.first, exhaustedResults.second)
        assertEquals(victoryResults.first, victoryResults.second)
        assertEquals(
            "समाप्ति",
            (exhaustedResults.second as SanskritValue.Rupa).fields.getValue("अवस्था").toDisplayText(),
        )
        assertEquals(
            "विजय",
            (victoryResults.second as SanskritValue.Rupa).fields.getValue("अवस्था").toDisplayText(),
        )
    }

    @Test
    fun `bounded compiled loop publishes and pipes its exhaustion outcome`() {
        val source = """
            हृ + ल्युट् + सुँ ।
            अवस्था + अम् एक + अम् च वि + युज् + णिच् + लोट् + सिप् ततः दा + लोट् + सिप् फल + अम् अवस्था + ङे ॥

            त्रि + अम् अवस्था + ङे दा + लोट् + सिप् ।
            द्वि + कृत्वः यावत् अवस्था + अम् शून्य + अम् च विद् + लोट् + सिप् तावत् हृ + ल्युट् + टा कृ + लोट् + सिप् अन्यथा समाप्तम् + अम् मुद्र् + लोट् + सिप् ततः मुद्र् + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
            .filterIsInstance<ExecutionResult.Success>().last().typedValue
        val generated = BytecodeCompiler.compileAndLoad(source, "CompiledLoopOutcome")
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
        val outcome = values.getValue("परिणाम") as SanskritValue.Rupa

        assertEquals(interpreted, values.getValue("LastResult"))
        assertEquals("समाप्ति", outcome.fields.getValue("अवस्था").toDisplayText())
        assertEquals(2L, (outcome.fields.getValue("प्रयत्नसङ्ख्या") as SanskritValue.Sankhya).value)
    }

    @Test
    fun `break signal exits the nearest compiled repetition`() {
        val source = """
            प्रयत्न + ल्युट् + सुँ ।
            वि + स्था + लोट् + सिप् ॥

            पञ्च + कृत्वः प्रयत्न + ल्युट् + टा कृ + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = PaniniVM().evalScript(source)
            .filterIsInstance<ExecutionResult.Success>()
            .single { it.controlSignal != null }
            .typedValue
        val generated = BytecodeCompiler.compileAndLoad(source, "CompiledBreakRepetition")
        @Suppress("UNCHECKED_CAST")
        val compiled = (generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>)
            .getValue("LastResult")

        assertEquals(interpreted, compiled)
    }

    @Test
    fun `compiled non-halting loop obeys an explicit host budget`() {
        val source = """
            एक + अम् अवस्था + ङे दा + लोट् + सिप् ।
            यावत् अवस्था + अम् शून्य + अम् च विद् + लोट् + सिप् तावत् एक + अम् अवस्था + ङे दा + लोट् + सिप् ।
        """.trimIndent()
        val generated = BytecodeCompiler.compileAndLoad(source, "CompiledBudgetedLoop")

        val thrown = assertFailsWith<InvocationTargetException> {
            generated.getMethod("execute", java.lang.Long.TYPE).invoke(null, 3L)
        }

        val limit = thrown.targetException as CompiledExecutionLimitExceededException
        assertTrue(limit.message.orEmpty().contains("3 iterations"))
    }

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
    fun `pipeline result enters a typed generated samjna operation`() {
        val source = """
            वर्धन + ल्युट् + सुँ ।
            मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            सङ्ख्या + सुँ इति परिणाम + सुँ ।
            मान + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥

            एक + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ततः वर्धन + ल्युट् + टा कृ + लोट् + सिप् ।
        """.trimIndent()
        val interpretedResults = PaniniVM().evalScript(source)
        assertTrue(
            interpretedResults.none { it is ExecutionResult.Failure },
            interpretedResults.joinToString(),
        )
        val interpreted = interpretedResults.filterIsInstance<ExecutionResult.Success>().last().typedValue
        val generated = BytecodeCompiler.compileAndLoad(source, "CompiledPipedSamjna")
        @Suppress("UNCHECKED_CAST")
        val compiled = (generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>)
            .getValue("LastResult")

        assertEquals(interpreted, compiled)
        assertEquals(4L, (compiled as SanskritValue.Sankhya).value)
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
        val outcome = values.getValue("परिणाम") as SanskritValue.Rupa
        assertEquals("विजय", outcome.fields.getValue("अवस्था").toDisplayText())
        assertEquals(7L, (outcome.fields.getValue("प्रयत्नसङ्ख्या") as SanskritValue.Sankhya).value)
    }
}
