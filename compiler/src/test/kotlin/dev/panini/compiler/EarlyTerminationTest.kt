package dev.panini.compiler

import dev.panini.execution.*
import kotlin.test.*

class EarlyTerminationTest {
    @Test fun `reported-condition loop can break without first producing truth`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            वि + स्था + लोट् + सिप् ॥
            त्रि + कृत्वसुच् यावत् फल + सुँ न तावत् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val results = PaniniVM().evalScript(source)
        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        assertEquals(1L, assertIs<ExecutionResult.Success>(results.last()).iterationCount)
        val generated = BytecodeCompiler.compileAndLoad(source, "BreakBeforeTruth")
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
        assertEquals(1L, assertIs<SanskritValue.Sankhya>(values["प्रयत्नसङ्ख्या"]).value)
    }

    @Test fun `inner while break does not stop enclosing foreach`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            वि + स्था + लोट् + सिप् ॥
            एक + सुँ द्वि + औ त्रि + जस् च + इति सूची + सुँ असँ + लट् + तिप् ।
            सूची + ङस् सदस्य + अम् प्रति यावत् एक + सुँ एक + टा सम + सुँ असँ + लट् + तिप् तावत् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val results = PaniniVM().evalScript(source)
        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        assertEquals(3, results.filterIsInstance<ExecutionResult.Success>().count { it.operation == "pvm.while" })
        assertTrue(results.filterIsInstance<ExecutionResult.Success>().all { it.controlSignal == null })
        BytecodeCompiler.compileAndLoad(source, "WhileInsideForEach").getMethod("execute").invoke(null)
    }
    @Test fun `inner repetition consumes break before enclosing foreach and callbacks`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            वि + स्था + लोट् + सिप् ॥
            एक + सुँ द्वि + औ त्रि + जस् च + इति सूची + सुँ असँ + लट् + तिप् ।
            सूची + ङस् सदस्य + अम् प्रति द्वि + कृत्वसुच् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val published = mutableListOf<ExecutionResult>()
        val results = PaniniVM().evalScript(source, onResult = published::add)
        assertEquals(4, results.size, results.toString())
        assertTrue((results + published).filterIsInstance<ExecutionResult.Success>().all { it.controlSignal == null })
        val generated = BytecodeCompiler.compileAndLoad(source, "NestedBreakScopes")
        generated.getMethod("execute").invoke(null)
    }
    @Test fun `procedure break skips later sentences and exits foreach once`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            वि + स्था + लोट् + सिप् ।
            नव + अम् मुद्र् + णिच् + लोट् + सिप् ॥
            एक + सुँ द्वि + औ त्रि + जस् च + इति सूची + सुँ असँ + लट् + तिप् ।
            सूची + ङस् सदस्य + अम् प्रति परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val results = PaniniVM().evalScript(source)
        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        assertEquals(2, results.size)
        assertTrue(results.filterIsInstance<ExecutionResult.Success>().none { it.value == "नव" })
        val generated = BytecodeCompiler.compileAndLoad(source, "EarlyBreakForEach")
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
        assertEquals(assertIs<ExecutionResult.Success>(results.last()).value,
            values.getValue("LastResult").toDisplayText())
    }

    @Test fun `failed procedure stops both later body sentences and repetition`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            सूची + ङस् सङ्ख्या + शस् युज् + णिच् + लोट् + सिप् ।
            नव + अम् मुद्र् + णिच् + लोट् + सिप् ॥
            राम + ङस् सीता + ङस् च सूची + सुँ असँ + लट् + तिप् ।
            त्रि + कृत्वसुच् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val results = PaniniVM().evalScript(source)
        assertEquals(1, results.filterIsInstance<ExecutionResult.Failure>().size, results.toString())
        assertIs<ExecutionResult.Failure>(results.last())
        assertTrue(results.filterIsInstance<ExecutionResult.Success>().none { it.value == "नव" })
        val generated = BytecodeCompiler.compileAndLoad(source, "FailedRepetition")
        val failure = assertFailsWith<java.lang.reflect.InvocationTargetException> {
            generated.getMethod("execute").invoke(null)
        }
        assertIs<CompiledPaniniExecutionException>(failure.cause)
    }
}
