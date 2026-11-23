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

/** Grammatical requirements checked while building the AST are located diagnostics, not crashes. */
class GrammarConstraintDiagnosticsTest {
    // reduction.pvm with the collection in the genitive plural, which a reduction does not accept.
    private val reduction = "सूची + आम् प्रथम + अम् आरभ्य सञ्चित + अम् सदस्य + अम् ग्रह् + क्त्वा " +
        "सञ्चित + अम् सदस्य + अम् च युज् + णिच् + लोट् + सिप् ।"

    @Test fun `a construction requirement is a GRAMMAR_CONSTRAINT at the utterance`() {
        val error = assertFailsWith<PaniniSemanticException> { PaniniParser().parse(reduction) }
        assertEquals(PaniniSemanticDiagnosticCode.GRAMMAR_CONSTRAINT, error.diagnostic.code)
        assertTrue("genitive singular collection" in error.diagnostic.message, error.diagnostic.message)
        assertEquals(0, error.diagnostic.span.start)
        assertEquals(1, error.diagnostic.line)
    }

    @Test fun `the interpreter returns the same code as a failure, with its location`() {
        val failure = assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(reduction).last())
        val diagnostic = assertNotNull(failure.diagnostic)
        assertEquals("GRAMMAR_CONSTRAINT", diagnostic.code)
        assertNotNull(diagnostic.span)
    }

    @Test fun `a run-time failure without a span is located at its sentence in the script`() {
        val first = "एक + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val second = "त्रयोदशन् + शस् अङ्क + ङि स्था + णिच् + लोट् + सिप् ।"
        val source = "$first\n$second"
        val failure = assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).last())
        val diagnostic = assertNotNull(failure.diagnostic)
        val span = assertNotNull(diagnostic.span)
        assertTrue(span.start > first.length, "span $span should fall in the second sentence")
        assertEquals(diagnostic.offendingText, source.substring(span.start, span.endExclusive))
    }

    @Test fun `a listener receives the failure already located`() {
        val source = "एक + अम् मुद्र् + णिच् + लोट् + सिप् ।\nत्रयोदशन् + शस् अङ्क + ङि स्था + णिच् + लोट् + सिप् ।"
        val published = mutableListOf<ExecutionResult>()
        val returned = PaniniVM().evalScript(source, onResult = { published += it }).last()
        val heard = assertIs<ExecutionResult.Failure>(published.last())
        assertEquals(assertIs<ExecutionResult.Failure>(returned).diagnostic, heard.diagnostic)
        assertNotNull(heard.diagnostic?.span)
    }

    @Test fun `a sentence without its verb is a located VERBLESS_SENTENCE, not a crash`() {
        val first = "एक + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val verbless = "वाम + सुँ सङ्ख्या + सुँ इति मान + सुँ"
        val source = "$first\n$verbless ।"
        val failure = assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).single())
        val diagnostic = assertNotNull(failure.diagnostic)
        assertEquals("VERBLESS_SENTENCE", diagnostic.code)
        val span = assertNotNull(diagnostic.span)
        assertEquals(verbless, source.substring(span.start, span.endExclusive))
        val error = assertFailsWith<PaniniSemanticException> { PaniniParser().parseDocument(source) }
        assertEquals(2, error.diagnostic.line)
    }

    @Test fun `unparsable source is a SYNTAX_ERROR at the offending token, not a crash`() {
        val source = "एक + अम् मुद्र् + णिच् + लोट् + सिप् ।\nयदि ।"
        val failure = assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).single())
        val diagnostic = assertNotNull(failure.diagnostic)
        assertEquals("SYNTAX_ERROR", diagnostic.code)
        val span = assertNotNull(diagnostic.span)
        assertTrue(span.start > source.indexOf('\n'), "span $span should fall on the second line")
    }

    @Test fun `a document reports the item where the requirement fails`() {
        val source = "एक + अम् मुद्र् + णिच् + लोट् + सिप् ।\n$reduction"
        val error = assertFailsWith<PaniniSemanticException> { PaniniParser().parseDocument(source) }
        assertEquals(PaniniSemanticDiagnosticCode.GRAMMAR_CONSTRAINT, error.diagnostic.code)
        assertEquals(2, error.diagnostic.line)
    }
}
