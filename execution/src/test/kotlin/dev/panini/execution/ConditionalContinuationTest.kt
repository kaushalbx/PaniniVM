package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ConditionalContinuationTest {
    private fun console(source: String): List<String> {
        val results = PaniniVM().evalScript(source)
        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        return results.filterIsInstance<ExecutionResult.Success>()
            .filter { it.outputKind == OutputKind.CONSOLE }
            .map { it.value }
    }

    @Test fun `tada sentences execute only after a true nearest condition`() {
        fun source(right: String) = """
            यदि एक + सुँ $right + टा सम + सुँ असँ + लट् + तिप्
            तर्हि एक + अम् मुद्र् + णिच् + लोट् + सिप् ।
            तदा द्वि + अम् मुद्र् + णिच् + लोट् + सिप् ।
            तदा त्रि + अम् मुद्र् + णिच् + लोट् + सिप् ।
        """.trimIndent()

        assertEquals(listOf("एक", "द्वि", "त्रि"), console(source("एक")))
        assertEquals(emptyList(), console(source("द्वि")))
    }

    @Test fun `alternate follows the complete multi sentence consequent`() {
        fun source(right: String) = """
            यदि एक + सुँ $right + टा सम + सुँ असँ + लट् + तिप्
            तर्हि एक + अम् मुद्र् + णिच् + लोट् + सिप् ।
            तदा द्वि + अम् मुद्र् + णिच् + लोट् + सिप् ।
            अन्यथा त्रि + अम् मुद्र् + णिच् + लोट् + सिप् ।
        """.trimIndent()

        assertEquals(listOf("एक", "द्वि"), console(source("एक")))
        assertEquals(listOf("त्रि"), console(source("द्वि")))
    }

    @Test fun `yada tada observes an event condition once and runs all continuations`() {
        fun source(right: String) = """
            यदा एक + सुँ $right + टा सम + सुँ असँ + लट् + तिप्
            तदा एक + अम् मुद्र् + णिच् + लोट् + सिप् ।
            तदा द्वि + अम् मुद्र् + णिच् + लोट् + सिप् ।
        """.trimIndent()

        assertEquals(listOf("एक", "द्वि"), console(source("एक")))
        assertEquals(emptyList(), console(source("द्वि")))
    }
}
