package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NaturalScriptStopTest {
    @Test
    fun `virama crosses nested loops and ends the script`() {
        val results = PaniniVM().evalScript(
            """
            एक + अम् मुद्र् + णिच् + लोट् + सिप् ।
            वि + रम् + लोट् + सिप् ।
            नवन् + अम् मुद्र् + णिच् + लोट् + सिप् ।
            """.trimIndent())

        assertTrue(results.any { it is ExecutionResult.Success && it.controlSignal == ExecutionControlSignal.HALT_SCRIPT }, results.toString())
        assertEquals(listOf("एक"), results.filterIsInstance<ExecutionResult.Success>()
            .filter { it.outputKind == OutputKind.CONSOLE }.map { it.value })
    }

    @Test
    fun `virama inside nested iteration skips all remaining script sentences`() {
        val source = java.io.File("examples/algorithms/two_sum.pvm").readText() +
            "\nनवन् + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val results = PaniniVM().evalScript(source)

        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        assertEquals(listOf("एक चतुर्"), results.filterIsInstance<ExecutionResult.Success>()
            .filter { it.outputKind == OutputKind.CONSOLE }.map { it.value })
    }
}
