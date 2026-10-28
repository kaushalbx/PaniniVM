package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

class NaturalActionCoordinationTest {
    private fun console(source: String): List<String> = PaniniVM().evalScript(source)
        .filterIsInstance<ExecutionResult.Success>()
        .filter { it.outputKind == OutputKind.CONSOLE }
        .map { it.value }

    @Test
    fun `cha coordinates complete finite actions in source order`() {
        val source = """
            यदि एक + सुँ एक + टा सम + सुँ असँ + लट् + तिप्
            तर्हि एक + अम् मुद्र् + णिच् + लोट् + सिप्
            च द्वि + अम् मुद्र् + णिच् + लोट् + सिप् ।
        """.trimIndent()

        assertEquals(listOf("एक", "द्वि"), console(source))
    }

    @Test
    fun `atha expresses explicit succession without result piping`() {
        val source = """
            यदि एक + सुँ एक + टा सम + सुँ असँ + लट् + तिप्
            तर्हि एक + अम् मुद्र् + णिच् + लोट् + सिप्
            अथ द्वि + अम् मुद्र् + णिच् + लोट् + सिप् ।
        """.trimIndent()

        assertEquals(listOf("एक", "द्वि"), console(source))
    }

    @Test
    fun `unprefixed imperative stha stops the nearest loop`() {
        val source = """
            त्रि + कृत्वसुच् एक + अम् मुद्र् + णिच् + क्त्वा स्था + लोट् + सिप् ।
            द्वि + अम् मुद्र् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val results = PaniniVM().evalScript(source)

        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        assertTrue(results.filterIsInstance<ExecutionResult.Success>().all { it.controlSignal == null })
        assertEquals(listOf("एक", "द्वि"), results.filterIsInstance<ExecutionResult.Success>()
            .filter { it.outputKind == OutputKind.CONSOLE }.map { it.value })
    }

    @Test
    fun `natural stop is consumed by the nearest nested loop`() {
        val source = """
            एक + सुँ द्वि + औ त्रि + जस् च + इति सूची + सुँ असँ + लट् + तिप् ।
            सूची + ङस् सदस्य + अम् प्रति
            त्रि + कृत्वसुच् सदस्य + अम् मुद्र् + णिच् + क्त्वा स्था + लोट् + सिप् ।
            नवन् + अम् मुद्र् + णिच् + लोट् + सिप् ।
        """.trimIndent()

        assertEquals(listOf("एक", "द्वि", "त्रि", "नवन्"), console(source))
    }

    @Test
    fun `control signal prevents later cha coordinated action`() {
        val source = """
            त्रि + कृत्वसुच् स्था + लोट् + सिप्
            च नवन् + अम् मुद्र् + णिच् + लोट् + सिप् ।
            एक + अम् मुद्र् + णिच् + लोट् + सिप् ।
        """.trimIndent()

        assertEquals(listOf("एक"), console(source))
    }

    @Test
    fun `unprefixed imperative stha remains an ordinary action outside loops`() {
        val result = assertIs<ExecutionResult.Success>(PaniniVM().eval("स्था + लोट् + सिप् ।"))

        assertNull(result.controlSignal)
        assertEquals("panini.eval", result.operation)
    }
}
