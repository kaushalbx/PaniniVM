package dev.panini.compiler

import dev.panini.execution.*
import kotlin.test.*

class ProcedureInvocationWordOrderParityTest {
    @Test fun `unique locative parameter can follow its value and finite verb`() {
        for ((index, phrase) in listOf("पञ्च + अम् मान + ङि परीक्षण + टा डुकृञ् + उ + लोट् + सिप्",
            "डुकृञ् + उ + लोट् + सिप् परीक्षण + टा पञ्च + अम् मान + ङि").withIndex()) {
            val source = """
                परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
                मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
                मान + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥
                $phrase ।
            """.trimIndent()
            val results = PaniniVM().evalScript(source)
            val expected = assertIs<ExecutionResult.Success>(results.last(), results.toString()).typedValue
            assertEquals(6L, assertIs<SanskritValue.Sankhya>(expected).value)
            val generated = BytecodeCompiler.compileAndLoad(source, "ReorderedUniqueNamedArgument$index")
            assertEquals(expected, (generated.getMethod("execute").invoke(null) as Map<*, *>)["LastResult"])
        }
    }

    @Test fun `scoped procedure domain can follow the procedure noun and finite verb`() {
        for ((index, phrase) in listOf("पञ्च + अम् परीक्षण + टा गणित + ङस् डुकृञ् + उ + लोट् + सिप्",
            "डुकृञ् + उ + लोट् + सिप् परीक्षण + टा पञ्च + अम् गणित + ङस्").withIndex()) {
            val source = """
                गणित + ङस् परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
                मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
                मान + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥
                $phrase ।
            """.trimIndent()
            val results = PaniniVM().evalScript(source)
            val expected = assertIs<ExecutionResult.Success>(results.last(), results.toString()).typedValue
            assertEquals(6L, assertIs<SanskritValue.Sankhya>(expected).value)
            val generated = BytecodeCompiler.compileAndLoad(source, "ReorderedScopedProcedure$index")
            val values = generated.getMethod("execute").invoke(null) as Map<*, *>
            assertEquals(expected, values["LastResult"])
        }
    }

    @Test fun `procedure noun and finite verb do not truncate written operands`() {
        for ((index, phrase) in listOf("परीक्षण + टा पञ्च + अम् डुकृञ् + उ + लोट् + सिप्",
            "डुकृञ् + उ + लोट् + सिप् परीक्षण + टा पञ्च + अम्").withIndex()) {
            val source = """
                परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
                मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
                मान + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥
                $phrase ।
            """.trimIndent()
            val results = PaniniVM().evalScript(source)
            val expected = assertIs<ExecutionResult.Success>(results.last(), results.toString()).typedValue
            assertEquals(6L, assertIs<SanskritValue.Sankhya>(expected).value)
            val generated = BytecodeCompiler.compileAndLoad(source, "ReorderedProcedureInvocation$index")
            val values = generated.getMethod("execute").invoke(null) as Map<*, *>
            assertEquals(expected, values["LastResult"])
        }
    }
}
