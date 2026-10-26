package dev.panini.execution

import dev.panini.vyakaranam.ast.Recurrence
import dev.panini.vyakaranam.ast.Sequence
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class PunahRepetitionTest {
    private val source = """
        शून्य + अम् गणना + ङि स्था + णिच् + लोट् + सिप् ।
        गणना + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ततः फल + अम् गणना + ङि स्था + णिच् + लोट् + सिप् ।
        पुनः गणना + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ततः फल + अम् गणना + ङि स्था + णिच् + लोट् + सिप् ।
    """.trimIndent()

    @Test
    fun `punah performs a compatible connected action again exactly once`() {
        val sentence = assertIs<PvmScriptStatement.Sentence>(PvmScript.parse(source)[2])
        val recurrence = assertIs<Recurrence>(sentence.program)
        assertEquals(2, assertIs<Sequence>(recurrence.body).statements.size)

        val results = PaniniVM().evalScript(source)
        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        val final = assertIs<ExecutionResult.Success>(results.last())
        assertEquals(2L, assertIs<SanskritValue.Sankhya>(final.typedValue).value)
    }

    @Test
    fun `punah requires a compatible completed antecedent`() {
        val failure = assertIs<ExecutionResult.Failure>(
            PaniniVM().evalScript("पुनः एक + अम् मुद्र् + णिच् + लोट् + सिप् ।").single(),
        )
        assertEquals(ExecutionError.INVALID_VALUE, failure.error)
        assertTrue(failure.message.contains("earlier completed action"))
    }

    @Test
    fun `punah retries only inside the selected conditional branch`() {
        fun console(right: String): List<String> = PaniniVM().evalScript(
            """
                एक + अम् मुद्र् + णिच् + लोट् + सिप् ।
                यदि एक + सुँ $right + टा सम + सुँ असँ + लट् + तिप्
                तर्हि पुनः एक + अम् मुद्र् + णिच् + लोट् + सिप्
                अन्यथा द्वि + अम् मुद्र् + णिच् + लोट् + सिप् ।
            """.trimIndent(),
        ).filterIsInstance<ExecutionResult.Success>()
            .filter { it.outputKind == OutputKind.CONSOLE }
            .map { it.value }

        assertEquals(listOf("एक", "एक"), console("एक"))
        assertEquals(listOf("एक", "द्वि"), console("द्वि"))
    }

    @Test
    fun `punah inside a bounded loop does not multiply its iteration count`() {
        val results = PaniniVM().evalScript(
            """
                एक + अम् मुद्र् + णिच् + लोट् + सिप् ।
                द्वि + कृत्वसुच् यावत् एक + सुँ एक + टा सम + सुँ असँ + लट् + तिप्
                तावत् पुनः एक + अम् मुद्र् + णिच् + लोट् + सिप् ।
            """.trimIndent(),
        )
        val console = results.filterIsInstance<ExecutionResult.Success>()
            .filter { it.outputKind == OutputKind.CONSOLE }.map { it.value }
        assertEquals(listOf("एक", "एक", "एक"), console)
    }

    @Test
    fun `punah rejects a competing numeric repetition count`() {
        val source = "पुनः त्रि + कृत्वसुच् एक + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val error = assertFailsWith<IllegalArgumentException> {
            PvmScript.parse(source)
        }
        assertTrue(error.message.orEmpty().contains("either an explicit repetition count or पुनः"))
        val failure = assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).single())
        val diagnostic = assertNotNull(failure.diagnostic)
        assertEquals("REPETITION_MARKER_CONFLICT", diagnostic.code)
        assertNotNull(diagnostic.span)
    }

    @Test
    fun `punah may qualify an action only once`() {
        val source = "पुनः पुनः एक + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val failure = assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).single())
        val diagnostic = assertNotNull(failure.diagnostic)
        assertEquals("REPEATED_PUNAH_MARKER", diagnostic.code)
        assertNotNull(diagnostic.span)
    }
}
