package dev.panini.compiler

import dev.panini.execution.*
import kotlin.test.*

class ProcedureLocalAssignmentTest {
    @Test fun `parameter writes are readable locally and do not replace caller bindings`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            एक + अम् मान + ङे दा + लोट् + सिप् ।
            मान + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥
            नव + अम् मान + ङे दा + लोट् + सिप् ।
            पञ्च + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val results = PaniniVM().evalScript(source)
        val interpreted = assertIs<ExecutionResult.Success>(results.last(), results.toString())
        assertEquals(2L, assertIs<SanskritValue.Sankhya>(interpreted.typedValue).value)
        val generated = BytecodeCompiler.compileAndLoad(source, "LocalParameterAssignment")
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
        assertEquals(interpreted.typedValue, values["LastResult"])
        assertEquals(9L, assertIs<SanskritValue.Sankhya>(values["मान"]).value)
        assertTrue(values.keys.none { it.startsWith("__pvm_parameter_") })
        val inspectCaller = source + "\nमान + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ।"
        assertEquals(10L, assertIs<SanskritValue.Sankhya>(
            assertIs<ExecutionResult.Success>(PaniniVM().evalScript(inspectCaller).last()).typedValue).value)
    }

    @Test fun `nested compiled frames update nearest owned slot`() {
        val runtime = CompiledProgramRuntime(mapOf("मान" to SanskritValue.Sankhya(9L, "नव")))
        runtime.enterFrame(arrayOf("मान"), arrayOf(SanskritValue.Sankhya(5L, "पञ्च")))
        runtime.enterFrame(arrayOf("मान"), arrayOf(SanskritValue.Sankhya(3L, "त्रि")))
        runtime.storeValue("मान", SanskritValue.Sankhya(1L, "एक"))
        assertEquals(1L, assertIs<SanskritValue.Sankhya>(runtime.loadValue("मान")).value)
        runtime.exitFrame()
        assertEquals(5L, assertIs<SanskritValue.Sankhya>(runtime.loadValue("मान")).value)
        runtime.exitFrame()
        assertEquals(9L, assertIs<SanskritValue.Sankhya>(runtime.loadValue("मान")).value)
    }

    @Test fun `positional reads and named writes share a parameter slot`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            एक + अम् मान + ङे दा + लोट् + सिप् ।
            प्रथम + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥
            पञ्च + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        assertParity(source, "AliasedLocalParameter", 2L)
    }

    @Test fun `nested procedure writes preserve the outer parameter slot`() {
        val source = """
            गणन + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            एक + अम् मान + ङे दा + लोट् + सिप् ।
            मान + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            त्रि + अम् मान + ङे दा + लोट् + सिप् ।
            द्वि + अम् गणन + टा डुकृञ् + उ + लोट् + सिप् ।
            मान + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥
            पञ्च + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        assertParity(source, "NestedLocalParameter", 4L)
    }

    @Test fun `foreach member shadows parameter and its writes remain in the iteration`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            सूची + ङस् मान + अम् प्रति एक + अम् मान + ङे दा + लोट् + सिप् ततः मान + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ।
            मान + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥
            एक + सुँ द्वि + औ त्रि + जस् च + इति सूची + सुँ असँ + लट् + तिप् ।
            पञ्च + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val results = PaniniVM().evalScript(source)
        val additions = results.filterIsInstance<ExecutionResult.Success>().filter {
            (it.typedValue as? SanskritValue.Sankhya)?.value == 2L
        }
        assertEquals(3, additions.size, results.toString())
        assertParity(source, "LocalMemberShadow", 6L)
    }

    private fun assertParity(source: String, name: String, expected: Long) {
        val results = PaniniVM().evalScript(source)
        val value = assertIs<ExecutionResult.Success>(results.last(), results.toString()).typedValue
        assertEquals(expected, assertIs<SanskritValue.Sankhya>(value).value)
        val generated = BytecodeCompiler.compileAndLoad(source, name)
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
        assertEquals(value, values["LastResult"])
    }
}
