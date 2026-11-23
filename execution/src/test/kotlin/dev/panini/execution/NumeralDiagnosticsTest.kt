package dev.panini.execution

import dev.panini.vyakaranam.parser.PaniniParser
import dev.panini.vyakaranam.parser.PaniniSemanticDiagnosticCode
import dev.panini.vyakaranam.parser.PaniniSemanticException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/** Numeral errors are typed, source-located diagnostics with a fix, never a crash. */
class NumeralDiagnosticsTest {
    private fun failure(source: String): ExecutionResult.Failure =
        assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).last())

    @Test fun `a primitive numeral in the wrong number is a parser diagnostic with its position`() {
        val source = "द्वि + सुँ मुद्र् + णिच् + लोट् + सिप् ।"
        val error = assertFailsWith<PaniniSemanticException> { PaniniParser().parse(source) }
        assertEquals(PaniniSemanticDiagnosticCode.NUMERAL_NUMBER_AGREEMENT, error.diagnostic.code)
        assertEquals("द्वि+सुँ", error.diagnostic.offendingText)
        assertEquals(0, error.diagnostic.span.start)
        assertTrue("dual" in error.diagnostic.message && "द्वि + औ" in error.diagnostic.message, error.diagnostic.message)

        val result = failure(source)
        assertEquals("NUMERAL_NUMBER_AGREEMENT", result.diagnostic?.code)
    }

    @Test fun `a segmented numeral in the wrong number is reported with a correction`() {
        val source = "एक + दशन् + अम् अङ्क + ङि स्था + णिच् + लोट् + सिप् ।"
        val diagnostic = assertNotNull(failure(source).diagnostic)
        assertEquals("NUMERAL_NUMBER_AGREEMENT", diagnostic.code)
        assertTrue("plural" in diagnostic.message, diagnostic.message)
        val span = assertNotNull(diagnostic.span)
        assertEquals("एक + दशन् + अम्", source.substring(span.start, span.endExclusive))
        assertEquals("एक + दशन् + शस्", diagnostic.corrections.single().replacement)
    }

    @Test fun `the editor reports a segmented numeral in the wrong number before the script runs`() {
        val source = "एक + दशन् + अम् अङ्क + ङि स्था + णिच् + लोट् + सिप् ।"
        val diagnostic = PrakriyaScriptValidator.validate(source).single { it.code == "NUMERAL_NUMBER_AGREEMENT" }
        assertEquals("एक + दशन् + अम्", source.substring(diagnostic.offset, diagnostic.offset + diagnostic.length))
        assertEquals("एक + दशन् + शस्", diagnostic.replacement)
        assertTrue(PrakriyaScriptValidator.validate("एक + दशन् + शस् अङ्क + ङि स्था + णिच् + लोट् + सिप् ।")
            .none { it.code == "NUMERAL_NUMBER_AGREEMENT" })
    }

    @Test fun `a one-word compound number names its segmented spelling`() {
        val diagnostic = assertNotNull(failure("त्रयोदशन् + शस् अङ्क + ङि स्था + णिच् + लोट् + सिप् ।").diagnostic)
        assertEquals(NumeralDiagnostics.SEGMENTED_NUMERAL, diagnostic.code)
        assertEquals("त्रयोदशन्", diagnostic.offendingText)
        assertTrue("त्रि + दशन्" in diagnostic.message, diagnostic.message)
    }
}
