package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

/** An operand requirement that fails on one written pada is reported at that pada, with its own code. */
class PadaConstraintDiagnosticsTest {
    private fun located(source: String): Pair<String, String> {
        val failure = assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).last(), source)
        val diagnostic = assertNotNull(failure.diagnostic, source)
        val span = assertNotNull(diagnostic.span, source)
        return diagnostic.code to source.substring(span.start, span.endExclusive)
    }

    @Test fun `a superlative without its locative group is marked on the superlative`() {
        assertEquals(PadaConstraintCode.MISSING_NIRDHARANA_GROUP to "अधिक + तमप् + ङस्",
            located("सूची + अम् अधिक + तमप् + ङस् स्थान + अम् अनु + इष् + लोट् + सिप् ।"))
    }

    @Test fun `a second copular predicate is marked on that predicate`() {
        assertEquals(PadaConstraintCode.AMBIGUOUS_COPULAR_PREDICATE to "न्यून + सुँ",
            located("यदि सदस्य + सुँ सम + सुँ न्यून + सुँ असँ + लट् + तिप् तर्हि सत्य + सुँ असँ + लट् + तिप् ।"))
    }

    @Test fun `a numeral in the wrong number inside a condition is marked on the numeral`() {
        assertEquals("NUMERAL_NUMBER_AGREEMENT" to "द्वि + टा",
            located("यदि एक + सुँ द्वि + टा त्रि + टा सम + सुँ असँ + लट् + तिप् तर्हि सत्य + सुँ असँ + लट् + तिप् ।"))
    }

    @Test fun `stems that are no numeral bound a range only as one compound name, which the fix writes`() {
        val source = "पञ्चन् + जस् एक + सुँ त्रि + जस् इति दत्त + सुँ नाम सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।\n" +
            "द्वि + औट् वाम-दक्षिण + ङि स्था + णिच् + लोट् + सिप् ।\n" +
            "दत्त + ङस् वाम + दक्षिण + ङसिँ त्रि + शस् परि + अन्त + अम् अंश + अम् ग्रहँ + श्ना + लोट् + सिप् ।\n" +
            "फल + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        assertEquals(PadaConstraintCode.UNKNOWN_RANGE_BOUNDARY to "वाम + दक्षिण + ङसिँ",
            located(source.substringBeforeLast("\n")))
        val failure = PaniniVM().evalScript(source).filterIsInstance<ExecutionResult.Failure>().single()
        val fix = assertNotNull(failure.diagnostic?.corrections?.singleOrNull())
        assertEquals("वाम + दक्षिण + ङसिँ", source.substring(fix.span.start, fix.span.endExclusive))
        val corrected = source.replaceRange(fix.span.start, fix.span.endExclusive, fix.replacement)
        val shown = assertIs<ExecutionResult.Success>(PaniniVM().evalScript(corrected).last())
        assertEquals("[एक, त्रि]", shown.value)
    }
}
