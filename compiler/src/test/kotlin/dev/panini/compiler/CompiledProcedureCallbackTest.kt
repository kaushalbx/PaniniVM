package dev.panini.compiler

import dev.panini.execution.ExecutionResult
import dev.panini.execution.PaniniVM
import dev.panini.execution.SanskritValue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Compiled collection actions call a program's procedures back as the interpreter does. */
class CompiledProcedureCallbackTest {
    private val list = "पञ्चन् + जस् एक + सुँ चतुर् + जस् द्वि + औ इति दत्त + सुँ नाम सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।\n"

    private fun agree(source: String, name: String): SanskritValue {
        val results = PaniniVM().evalScript(source)
        assertTrue(results.none { it is ExecutionResult.Failure }, results.toString())
        val interpreted = (results.last() as ExecutionResult.Success).typedValue
        @Suppress("UNCHECKED_CAST")
        val compiled = (BytecodeCompiler.compileAndLoad(source, name).getMethod("execute").invoke(null)
            as Map<String, SanskritValue>).getValue("LastResult")
        assertEquals(interpreted, compiled)
        return compiled
    }

    @Test fun `filter by a procedure reading its caller's bound`() {
        val source = "लघु + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।\n" +
            "सदस्य + सुँ नाम मान + सुँ सङ्ख्या + सुँ असँ + लट् + तिप् ।\n" +
            "यदि सदस्य + सुँ सीमा + ङसिँ न्यून + सुँ असँ + लट् + तिप् तर्हि फल + सुँ सत्य + सुँ असँ + लट् + तिप्\n" +
            "अन्यथा फल + सुँ असत्य + सुँ असँ + लट् + तिप् ॥\n" + list +
            "त्रि + शस् सीमा + ङि स्था + णिच् + लोट् + सिप् ।\n" +
            "दत्त + अम् लघु + टा वि + वृजीँ + लोट् + सिप् ।"
        assertEquals("[एक, द्वि]", agree(source, "CompiledCallbackFilter").toDisplayText())
    }

    @Test fun `map by a procedure`() {
        val source = "द्विगुण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।\n" +
            "सदस्य + सुँ नाम मान + सुँ सङ्ख्या + सुँ असँ + लट् + तिप् ।\n" +
            "सदस्य + अम् सदस्य + अम् च युज् + णिच् + लोट् + सिप् ॥\n" + list +
            "दत्त + अम् द्विगुण + टा सम् + यु + लोट् + सिप् ।"
        assertEquals(listOf(10L, 2L, 8L, 4L),
            (agree(source, "CompiledCallbackMap") as SanskritValue.Suchi).items.map { (it as SanskritValue.Sankhya).value })
    }
}
