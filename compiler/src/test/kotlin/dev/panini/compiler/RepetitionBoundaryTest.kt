package dev.panini.compiler

import dev.panini.execution.*
import kotlin.test.*

class RepetitionBoundaryTest {
    @Test fun `zero repetition does not execute or replace prior result`() {
        val published = mutableListOf<ExecutionResult>()
        val source = "नव + अम् पूर्व + ङे दा + लोट् + सिप् ।\nशून्य + कृत्वसुच् एक + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val results = PaniniVM().evalScript(source, onResult = published::add)
        assertEquals(1, results.size)
        assertTrue(published.filterIsInstance<ExecutionResult.Success>().none { it.outputKind == OutputKind.CONSOLE })
        val generated = BytecodeCompiler.compileAndLoad(source, "ZeroRepetition")
        @Suppress("UNCHECKED_CAST")
        val values = generated.getMethod("execute").invoke(null) as Map<String, SanskritValue>
        assertEquals(9L, assertIs<SanskritValue.Sankhya>(values["LastResult"]).value)
        val direct = assertIs<ExecutionResult.Success>(PaniniVM().eval(
            "शून्य + कृत्वसुच् एक + अम् मुद्र् + णिच् + लोट् + सिप् ।"))
        assertEquals("panini.evalScript", direct.operation)
    }

    @Test fun `one repetition is a loop boundary inside foreach`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            वि + स्था + लोट् + सिप् ॥
            एक + सुँ द्वि + औ त्रि + जस् च + इति सूची + सुँ असँ + लट् + तिप् ।
            सूची + ङस् सदस्य + अम् प्रति एक + कृत्वसुच् परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val results = PaniniVM().evalScript(source)
        assertEquals(4, results.size, results.toString())
        assertTrue(results.filterIsInstance<ExecutionResult.Success>().all { it.controlSignal == null })
        BytecodeCompiler.compileAndLoad(source, "SingleRepetitionScope").getMethod("execute").invoke(null)
    }
}
