package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class NaturalScriptStopTest {
    @Test fun `script stop requires the canonical ram identity rather than an ambiguous spelling`() {
        val parser = dev.panini.vyakaranam.parser.PaniniParser()
        for ((root, expected) in listOf("रमुँ" to true, "रमँ" to false, "रम्" to false)) {
            val invocation = parser.parse("वि + $root + लोट् + सिप् ।").body as dev.panini.vyakaranam.ast.Invocation
            assertEquals(expected, NaturalScriptStop.matches(invocation), root)
        }
        for (source in listOf("रमुँ + लोट् + सिप् ।", "वि + रमुँ + श्नु + लोट् + सिप् ।",
            "वि + रमुँ + णिच् + लोट् + सिप् ।", "वि + रमुँ + लट् + तिप् ।")) {
            kotlin.test.assertFalse(NaturalScriptStop.matches(parser.parse(source).body as dev.panini.vyakaranam.ast.Invocation), source)
        }
    }

    @Test fun `script stop cannot escape from a procedure body`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            वि + रमुँ + लोट् + सिप् ॥
            परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val failure = assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).last())
        val diagnostic = assertNotNull(failure.diagnostic)
        assertEquals("SCRIPT_STOP_IN_PROCEDURE", diagnostic.code)
        assertNotNull(diagnostic.span)
    }

    @Test
    fun `virama crosses nested loops and ends the script`() {
        val results = PaniniVM().evalScript(
            """
            एक + अम् मुद्र् + णिच् + लोट् + सिप् ।
            वि + रमुँ + लोट् + सिप् ।
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
