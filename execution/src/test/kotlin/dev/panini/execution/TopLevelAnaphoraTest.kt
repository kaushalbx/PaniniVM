package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Top-level तद् takes up a participant of the immediately preceding top-level sentence. */
class TopLevelAnaphoraTest {
    private fun values(source: String): List<String> = PaniniVM().evalScript(source).map {
        assertIs<ExecutionResult.Success>(it).value
    }

    /** A typed, source-located failure: the language reports it, it does not crash. */
    private fun assertDiagnostic(code: String, result: ExecutionResult) {
        val failure = assertIs<ExecutionResult.Failure>(result)
        val diagnostic = kotlin.test.assertNotNull(failure.diagnostic, failure.toString())
        assertEquals(code, diagnostic.code, failure.toString())
        kotlin.test.assertNotNull(diagnostic.span, "the diagnostic points at the pronoun")
        assertEquals("तद्+अम्", diagnostic.offendingText)
    }

    private inline fun <reified T> assertIs(value: Any?): T {
        assertTrue(value is T, value.toString())
        return value as T
    }

    @Test fun `a pronoun names the live variable of the preceding sentence`() {
        val source = """
            पञ्चन् + शस् सङ्ख्या + ङि स्था + णिच् + लोट् + सिप् ।
            तद् + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ततः फल + अम् तद् + ङि स्था + णिच् + लोट् + सिप् ।
            सङ्ख्या + अम् मुद्र् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        assertEquals("षट्", values(source).last())
    }

    @Test fun `the readable text declines the pronoun in its antecedent's gender`() {
        val source = """
            पञ्चन् + शस् सङ्ख्या + ङि स्था + णिच् + लोट् + सिप् ।
            तद् + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ततः फल + अम् तद् + ङि स्था + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val spoken = PvmUktiSadhaka().sadhayaScript(source)
        assertTrue("ताम् एकं च योजय ततः फलं तस्यां स्थापय ।" in spoken, spoken)
    }

    @Test fun `only the immediately preceding sentence supplies antecedents`() {
        val source = """
            पञ्चन् + शस् सङ्ख्या + ङि स्था + णिच् + लोट् + सिप् ।
            राम + अम् मुद्र् + णिच् + लोट् + सिप् ।
            तद् + अम् मुद्र् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        // राम (masculine, lexical) is the only participant of the sentence before; सङ्ख्या is not reachable.
        assertEquals("राम", values(source).last())
    }

    @Test fun `a missing antecedent fails instead of printing a literal pronoun`() {
        val failure = PaniniVM().evalScript("तद् + अम् मुद्र् + णिच् + लोट् + सिप् ।").last()
        assertTrue(failure is ExecutionResult.Failure, failure.toString())
    }

    @Test fun `two agreeing antecedents are ambiguous, never resolved by recency`() {
        assertDiagnostic("AMBIGUOUS_ANTECEDENT", PaniniVM().evalScript("सङ्ख्या + अम् गणना + अम् च युज् + णिच् + लोट् + सिप् ।\nतद् + अम् मुद्र् + णिच् + लोट् + सिप् ।").last())
    }

    @Test fun `an antecedent without lexical gender evidence is not guessed`() {
        assertDiagnostic("INCOMPLETE_AGREEMENT", PaniniVM().evalScript("पञ्चन् + शस् अङ्क + ङि स्था + णिच् + लोट् + सिप् ।\nतद् + अम् मुद्र् + णिच् + लोट् + सिप् ।").last())
    }

    @Test fun `number agreement selects among antecedents`() {
        // सङ्ख्या is singular here and the pronoun plural, so nothing agrees and the reference fails.
        val failure = PaniniVM().evalScript(
            "पञ्चन् + शस् सङ्ख्या + ङि स्था + णिच् + लोट् + सिप् ।\nतद् + शस् मुद्र् + णिच् + लोट् + सिप् ।").last()
        assertTrue(failure is ExecutionResult.Failure, failure.toString())
    }
}
