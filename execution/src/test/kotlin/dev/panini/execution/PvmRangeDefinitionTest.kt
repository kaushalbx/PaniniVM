package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class PvmRangeDefinitionTest {
    @Test
    fun `parses one scoped inclusive range`() {
        val statement = PvmScript.parse("एक + ङसिँ दशन् + शस् परि + अन्त + अम् इति सीमा + सुँ ।").single()
        val definition = assertIs<PvmScriptStatement.RangeDefinition>(statement, statement.toString())

        assertEquals(1L, definition.range.minimum.value)
        assertEquals(10L, definition.range.maximum.value)
    }

    @Test
    fun `locative upper bound is rejected as a range declaration`() {
        val source = "एक + ङसिँ दश + ङि इति सीमा + सुँ ।"

        assertIs<PvmScriptStatement.Sentence>(PvmScript.parse(source).single())
        assertTrue(PrakriyaScriptValidator.validate(source).any { "पर्यन्त" in it.message })
    }

    @Test
    fun `scoped range drives choice and quoted output but not ordinary printing`() {
        val results = PaniniVM().evalScript(
            """
            एक + ङसिँ दशन् + शस् परि + अन्त + अम् इति सीमा + सुँ ।
            दिव् + णिच् + लोट् + सिप् ततः रहस्य + ङे दा + लोट् + सिप् ।
            सङ्ख्या + अम् अनुमिनु + लोट् + सिप् इति मुद्र् + णिच् + लोट् + सिप् ।
            समाप्ताः + अम् मुद्र् + णिच् + लोट् + सिप् ।
            """.trimIndent(),
        )
        val successes = results.filterIsInstance<ExecutionResult.Success>()

        assertTrue(assertIs<SanskritValue.Sankhya>(successes.first().typedValue).value in 1L..10L)
        assertEquals(
            listOf("एकतः दशपर्यन्तं सङ्ख्याम् अनुमिनु", "समाप्ताः"),
            successes.filter { it.outputKind == OutputKind.CONSOLE }.map { it.value },
        )
    }
}
