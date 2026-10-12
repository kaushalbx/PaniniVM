package dev.panini.compiler

import dev.panini.execution.*
import java.lang.reflect.InvocationTargetException
import kotlin.test.*

class ProcedureNumericValidityTest {
    @Test fun `undefined host fraction cannot enter a numeric procedure parameter`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            एक + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ॥
            वस्तु + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        assertRejected(source, "InvalidNumericParameter")
    }

    @Test fun `undefined host fraction cannot satisfy a numeric procedure result`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ सङ्ख्या + अम् जन् + णिच् + लट् + तिप् ।
            वस्तु + अम् परिणाम + ङे दा + लोट् + सिप् ॥
            परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        assertRejected(source, "InvalidNumericResult")
    }

    private fun assertRejected(source: String, name: String) {
        val initial = mapOf("वस्तु" to SanskritValue.Rational(1, 0, "undefined"))
        val results = PaniniVM().evalScript(source,
            scope = ExecutionScope(environment = ValueEnvironment(initial)))
        assertIs<ExecutionResult.Failure>(results.last(), results.toString())
        val generated = BytecodeCompiler.compileAndLoad(source, name)
        val failure = assertFailsWith<InvocationTargetException> {
            generated.getMethod("execute", Map::class.java).invoke(null, initial)
        }
        assertIs<CompiledPaniniExecutionException>(failure.cause)
        val valid = mapOf("वस्तु" to SanskritValue.Rational(1, -2, "negative half"))
        val success = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source,
            scope = ExecutionScope(environment = ValueEnvironment(valid))).last())
        val values = generated.getMethod("execute", Map::class.java).invoke(null, valid) as Map<*, *>
        assertEquals(success.typedValue, values["LastResult"])
    }
}
