package dev.panini.compiler

import dev.panini.execution.*
import java.lang.reflect.InvocationTargetException
import kotlin.test.*

class ProcedureResultContractTest {
    @Test fun `malformed result relation has interpreter compiler diagnostic parity`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            फल + सुँ शून्य + सुँ असँ + लट् + तस् ॥
            परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val interpreted = assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).last())
        assertEquals("PROCEDURE_RESULT_AGREEMENT",
            assertNotNull(interpreted.diagnostic, interpreted.toString()).code)
        val compiled = assertFailsWith<CompilerUnsupportedException> {
            BytecodeCompiler.compile(source, "MalformedProcedureResultAgreement")
        }
        assertEquals("PROCEDURE_RESULT_AGREEMENT", assertNotNull(compiled.diagnostic).code)
    }

    @Test fun `procedure return belongs to callee across binding frames not loops or caller`() {
        val runtime = CompiledProgramRuntime()
        assertFailsWith<CompiledPaniniExecutionException> {
            runtime.requestProcedureReturn(SanskritValue.Sankhya(1L, "एक"))
        }
        runtime.enterFrame(emptyArray(), emptyArray())
        runtime.enterFrame(emptyArray(), emptyArray())
        runtime.enterBindingFrame(arrayOf("सदस्य"), arrayOf(SanskritValue.Sankhya(2L, "द्वि")))
        runtime.requestProcedureReturn(SanskritValue.Sankhya(1L, "एक"))
        assertTrue(runtime.isProcedureReturnRequested())
        assertFalse(runtime.consumeBreak())
        assertTrue(runtime.isProcedureReturnRequested())
        runtime.exitFrame()
        assertTrue(runtime.isProcedureExitRequested())
        runtime.requireProcedureResult("NUMBER")
        runtime.exitFrame()
        assertFalse(runtime.isProcedureExitRequested())
        runtime.requireProcedureResult("NUMBER")
        runtime.exitFrame()
    }

    @Test fun `nested early break cannot satisfy numeric result with caller value`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            सङ्ख्या + सुँ इति परिणाम + सुँ ।
            वि + स्था + लोट् + सिप् ॥
            गणन + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            सङ्ख्या + सुँ इति परिणाम + सुँ ।
            परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
            नव + अम् मुद्र् + णिच् + लोट् + सिप् ॥
            एक + अम् पूर्व + ङे दा + लोट् + सिप् ।
            गणन + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val results = PaniniVM().evalScript(source)
        assertIs<ExecutionResult.Failure>(results.last())
        assertTrue(results.filterIsInstance<ExecutionResult.Success>().none { it.value == "नव" })
        val generated = BytecodeCompiler.compileAndLoad(source, "NestedEarlyNumericContract")
        assertIs<CompiledPaniniExecutionException>(assertFailsWith<InvocationTargetException> {
            generated.getMethod("execute").invoke(null)
        }.cause)
    }

    @Test fun `break consumed by local repetition permits subsequent procedure result`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            सङ्ख्या + सुँ इति परिणाम + सुँ ।
            द्वि + कृत्वसुच् वि + स्था + लोट् + सिप् ।
            एक + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥
            परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val results = PaniniVM().evalScript(source)
        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        val interpreted = assertIs<ExecutionResult.Success>(results.last())
        assertEquals(2L, assertIs<SanskritValue.Sankhya>(interpreted.typedValue).value)
        assertNull(interpreted.controlSignal)
        val generated = BytecodeCompiler.compileAndLoad(source, "ConsumedBreakNumericContract")
        val values = generated.getMethod("execute").invoke(null) as Map<*, *>
        assertEquals(interpreted.typedValue, values["LastResult"])
    }

    @Test fun `caller result cannot satisfy missing declared result`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            सङ्ख्या + सुँ इति परिणाम + सुँ ।
            शून्य + कृत्वसुच् एक + अम् मुद्र् + णिच् + लोट् + सिप् ॥
            नव + अम् पूर्व + ङे दा + लोट् + सिप् ।
            परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).last())
        val generated = BytecodeCompiler.compileAndLoad(source, "MissingProcedureResult")
        val failure = assertFailsWith<InvocationTargetException> {
            generated.getMethod("execute").invoke(null)
        }
        assertTrue(assertIs<CompiledPaniniExecutionException>(failure.cause).message.contains("produced none"))
    }

    @Test fun `generated nested typed calls publish fresh results`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            सङ्ख्या + सुँ इति परिणाम + सुँ ।
            एक + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥
            गणन + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            सङ्ख्या + सुँ इति परिणाम + सुँ ।
            परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ॥
            गणन + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val results = PaniniVM().evalScript(source)
        val interpreted = assertIs<ExecutionResult.Success>(results.last(), results.toString())
        val generated = BytecodeCompiler.compileAndLoad(source, "NestedTypedResults")
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
        assertEquals(interpreted.typedValue, values["LastResult"])
    }

    @Test fun `fresh equal result counts and nested calls produce results`() {
        val runtime = CompiledProgramRuntime(mapOf("LastResult" to SanskritValue.Sankhya(1L, "एक")))
        runtime.enterFrame(emptyArray(), emptyArray())
        runtime.enterFrame(emptyArray(), emptyArray())
        runtime.storeValue("LastResult", SanskritValue.Sankhya(1L, "एक"))
        runtime.requireProcedureResult("NUMBER")
        runtime.exitFrame()
        runtime.requireProcedureResult("NUMBER")
        runtime.exitFrame()
    }

    @Test fun `fresh result must match declared kind`() {
        val runtime = CompiledProgramRuntime()
        runtime.enterFrame(emptyArray(), emptyArray())
        runtime.storeValue("LastResult", SanskritValue.Shabda("text"))
        assertFailsWith<CompiledPaniniExecutionException> { runtime.requireProcedureResult("NUMBER") }
    }

    @Test fun `scoped binding frames do not manufacture results`() {
        val runtime = CompiledProgramRuntime(mapOf("LastResult" to SanskritValue.Sankhya(1L, "एक")))
        runtime.enterFrame(emptyArray(), emptyArray())
        runtime.enterBindingFrame(arrayOf("सदस्य"), arrayOf(SanskritValue.Sankhya(2L, "द्वि")))
        runtime.exitFrame()
        assertFailsWith<CompiledPaniniExecutionException> { runtime.requireProcedureResult("NUMBER") }
    }
}
