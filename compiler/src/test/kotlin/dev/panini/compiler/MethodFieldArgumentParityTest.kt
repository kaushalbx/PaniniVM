package dev.panini.compiler

import dev.panini.execution.*
import kotlin.test.*

class MethodFieldArgumentParityTest {
    @Test fun `record method can receive a field argument after its method name`() {
        val source = """
            शब्द + मतुप् + ङस् मूल्य + सुँ पञ्च + सुँ असँ + लट् + तिप् ।
            गुण + मतुप् + ङस् परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            मान + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥
            गुण + मतुप् + ङस् परीक्षण + टा शब्द + मतुप् + ङस् मूल्य + अम् डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val results = PaniniVM().evalScript(source)
        val expected = assertIs<ExecutionResult.Success>(results.last(), results.toString()).typedValue
        assertEquals(6L, assertIs<SanskritValue.Sankhya>(expected).value)
        val generated = BytecodeCompiler.compileAndLoad(source, "MethodWithFollowingFieldArgument")
        assertEquals(expected, (generated.getMethod("execute").invoke(null) as Map<*, *>)["LastResult"])
    }
}
