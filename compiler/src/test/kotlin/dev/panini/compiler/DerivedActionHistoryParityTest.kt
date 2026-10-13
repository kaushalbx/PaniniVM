package dev.panini.compiler

import dev.panini.execution.*
import java.lang.reflect.InvocationTargetException
import kotlin.test.*

class DerivedActionHistoryParityTest {
    private val causative = "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।"
    private val plain = "द्वि + औट् त्रि + शस् च युज् + लोट् + सिप् ।"

    @Test fun `JVM budget exhaustion does not manufacture a qualified history event`() {
        val source = "यावत् एक + सुँ एक + टा सम + सुँ असँ + लट् + तिप् तावत् ${causative.removeSuffix("।")} ।"
        val lowered = CompilerFrontend.lower(source, "BudgetDerivedHistory")
        val identity = assertNotNull(lowered.entryPoint.filterIsInstance<CompilerInstruction.RecordActionFrame>()
            .single { it.dhatuUpadesha == "युजिँर्" }.actionIdentity)
        val program = CompilerProgram(lowered.className, emptyList(), listOf(
            CompilerProcedure("runBudget", lowered.entryPoint + CompilerInstruction.Return)))
        val generated = BytecodeCompiler.PaniniClassLoader(javaClass.classLoader)
            .loadFromBytes(program.className, CompilerProgramJvmEmitter.emit(program))
        val runtime = CompiledProgramRuntime(1L)
        val failure = assertFailsWith<InvocationTargetException> {
            generated.getMethod("runBudget", CompiledProgramRuntime::class.java).invoke(null, runtime)
        }
        assertIs<CompiledExecutionLimitExceededException>(failure.cause)
        assertEquals(3L, assertIs<SanskritValue.Sankhya>(runtime.loadActionResult(identity.compiledHistoryKey(), 1)).value)
        assertFailsWith<CompiledPaniniExecutionException> { runtime.loadActionResult(identity.compiledHistoryKey(), 2) }
    }

    @Test fun `qualified history remains chronological across recursive procedure frames`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            अवस्था + अम् एक + अम् च वि + युज् + णिच् + लोट् + सिप् ततः फल + अम् अवस्था + ङे दा + लोट् + सिप् ।
            गणन + टा डुकृञ् + उ + लोट् + सिप् ॥
            गणन + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            यदि अवस्था + अम् शून्य + अम् च विद् + लोट् + सिप् तर्हि परीक्षण + टा डुकृञ् + उ + लोट् + सिप् अन्यथा वि + स्था + लोट् + सिप् ॥
            त्रि + शस् अवस्था + ङे दा + लोट् + सिप् ।
            गणन + टा डुकृञ् + उ + लोट् + सिप् ।
            वि + युज् + णिच् + ल्युट् + ङस् प्रथम + अम् फल + अम् मुद्र् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val results = PaniniVM().evalScript(source)
        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        val expected = assertIs<ExecutionResult.Success>(results.last())
        assertEquals("द्वे", expected.value)
        val generated = BytecodeCompiler.compileAndLoad(source, "RecursiveDerivedHistory")
        val values = generated.getMethod("execute").invoke(null) as Map<*, *>
        assertEquals(expected.typedValue, values["LastResult"])
    }

    @Test fun `dynamic participant calls record exact source action identity`() {
        val inputs = listOf(
            mapOf("पूर्व" to SanskritValue.Sankhya(2L, "द्वि"), "उत्तर" to SanskritValue.Sankhya(3L, "त्रि")),
            mapOf("पूर्व" to SanskritValue.Suchi(listOf(SanskritValue.Sankhya(2L, "द्वि"))),
                "उत्तर" to SanskritValue.Suchi(listOf(SanskritValue.Shabda("राम")))))
        for ((index, head) in listOf("फल", "कर्मन्").withIndex()) {
            val source = "पूर्व + अम् उत्तर + अम् च गण + णिच् + लोट् + सिप् । " +
                "चतुर् + शस् पञ्चन् + शस् च गण + लोट् + सिप् । " +
                "गण + णिच् + ल्युट् + ङस् $head + अम् मुद्र् + णिच् + लोट् + सिप् ।"
            val ir = CompilerFrontend.lower(source, "DynamicDerivedHistoryIr$index")
            assertTrue(ir.entryPoint.filterIsInstance<CompilerInstruction.CallParticipantGroup>()
                .any { it.actionIdentity?.sanadi == listOf(dev.panini.core.SanadiAffix.NIC) })
            val generated = BytecodeCompiler.compileAndLoad(source, "DynamicDerivedHistory$index")
            for ((inputIndex, initial) in inputs.withIndex()) {
                val expected = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source,
                    scope = ExecutionScope(environment = ValueEnvironment(initial))).last())
                val values = generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<*, *>
                assertEquals(expected.typedValue, values["LastResult"])
                if (head == "फल") assertEquals(if (inputIndex == 0) "षट्" else "द्वे", expected.value)
            }
        }
    }

    @Test fun `prefix-qualified history does not alias a later unprefixed action`() {
        for ((index, head) in listOf("फल", "कर्मन्").withIndex()) {
            val source = "नवन् + शस् द्वि + औट् च वि + युज् + णिच् + लोट् + सिप् । " +
                "$causative वि + युज् + णिच् + ल्युट् + ङस् $head + अम् मुद्र् + णिच् + लोट् + सिप् ।"
            val expected = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source).last())
            val generated = BytecodeCompiler.compileAndLoad(source, "PrefixedHistory$index")
            val values = generated.getMethod("execute").invoke(null) as Map<*, *>
            assertEquals(expected.typedValue, values["LastResult"])
            if (head == "फल") assertEquals("सप्त", expected.value)
        }
    }

    @Test fun `qualified history selects the same result and participants in both backends`() {
        for ((index, head) in listOf("फल", "कर्मन्").withIndex()) {
            for ((orderIndex, qualifier) in listOf("", "प्रथम + अम् ").withIndex()) {
                val source = "$causative $plain युज् + णिच् + ल्युट् + ङस् $qualifier$head + अम् मुद्र् + णिच् + लोट् + सिप् ।"
                val expected = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source).last())
                val generated = BytecodeCompiler.compileAndLoad(source, "DerivedHistory$index$orderIndex")
                val values = generated.getMethod("execute").invoke(null) as Map<*, *>
                assertEquals(expected.typedValue, values["LastResult"])
            }
        }
    }

    @Test fun `unrelated root-family events cannot satisfy a qualified history ordinal`() {
        for ((index, head) in listOf("फल", "कर्मन्").withIndex()) {
            for ((orderIndex, qualifier) in listOf("द्वि + तीय + अम्", "पूर्व + अम्").withIndex()) {
                val source = "$causative $plain युज् + णिच् + ल्युट् + ङस् $qualifier $head + अम् मुद्र् + णिच् + लोट् + सिप् ।"
                assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).last())
                val generated = BytecodeCompiler.compileAndLoad(source, "MissingDerivedHistory$index$orderIndex")
                val failure = assertFailsWith<InvocationTargetException> { generated.getMethod("execute").invoke(null) }
                assertIs<CompiledPaniniExecutionException>(failure.cause)
            }
        }
    }
}
