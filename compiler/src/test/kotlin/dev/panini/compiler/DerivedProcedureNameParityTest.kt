package dev.panini.compiler

import dev.panini.execution.*
import java.lang.reflect.InvocationTargetException
import kotlin.test.*

class DerivedProcedureNameParityTest {
    @Test
    fun `segmented derived procedure name retains prefix and causative identity`() {
        val source = """
            वि + भज् + णिच् + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            अंश + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            हर + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            सङ्ख्या + सुँ इति परिणाम + सुँ ।
            न हर + अम् शून्य + अम् ।
            अंश + अम् हर + अम् च वि + भज् + णिच् + लोट् + सिप् ॥
            भाज्य + अम् भाजक + अम् च वि + भज् + णिच् + ल्युट् + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val generated = BytecodeCompiler.compileAndLoad(source, "DerivedProcedureNameParity")
        for (divisor in listOf(2L, 0L)) {
            val initial = mapOf("भाज्य" to SanskritValue.Sankhya(10, "host"),
                "भाजक" to SanskritValue.Sankhya(divisor, "host"))
            val results = PaniniVM().evalScript(source, scope = ExecutionScope(environment = ValueEnvironment(initial)))
            if (divisor == 0L) {
                val failure = assertIs<ExecutionResult.Failure>(results.last())
                assertTrue(failure.message.contains("निषेध"))
                val error = assertIs<CompiledPaniniExecutionException>(assertFailsWith<InvocationTargetException> {
                    generated.getMethod("execute", Map::class.java).invoke(null, initial)
                }.cause)
                assertEquals(failure.error, error.error)
            } else {
                val result = assertIs<ExecutionResult.Success>(results.last(), results.toString())
                assertEquals(5L, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
                assertEquals(result.typedValue,
                    (generated.getMethod("execute", Map::class.java).invoke(null, initial) as Map<*, *>)["LastResult"])
            }
        }
    }
}
