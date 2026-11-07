package dev.panini.execution

import dev.panini.analysis.FrameKarakaResolution
import dev.panini.analysis.karakaLabel
import dev.panini.core.Karaka
import dev.panini.execution.binding.VyakaranamExecutionAdapter
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SuperlativeTest {
    private fun n(value: Long) = SanskritValue.Sankhya(value, value.toString())
    private fun list(vararg values: Long) = SanskritValue.Suchi(values.map(::n))

    @Test fun `the least and the greatest are chosen, the first of equals winning`() {
        assertEquals(n(1), superlativeOf(list(5, 1, 4, 1), CopularPredicate.LESS_THAN))
        assertEquals(n(9), superlativeOf(list(3, 9, 2, 9), CopularPredicate.GREATER_THAN))
        val equal = superlativeOf(SanskritValue.Suchi(listOf(SanskritValue.Sankhya(2, "first"), SanskritValue.Sankhya(2, "second"))),
            CopularPredicate.LESS_THAN)
        assertEquals("first", (equal as SanskritValue.Sankhya).word)
    }

    @Test fun `an empty group has no extreme member`() {
        assertEquals(SanskritValue.Lopa, superlativeOf(list(), CopularPredicate.LESS_THAN))
        assertFailsWith<IllegalArgumentException> { superlativeOf(n(3), CopularPredicate.LESS_THAN) }
        assertFailsWith<IllegalArgumentException> { ExecutionExpression.Superlative(ExecutionExpression.Reference("x"), CopularPredicate.EQUAL) }
    }

    @Test fun `the superlative is the karta and its locative group is no karaka`() {
        val parser = PaniniParser()
        val frame = VyakaranamExecutionAdapter.analyzeAsWritten(parser.parse(
            "उत्तर + सुप् न्यून + तमप् + सुँ सदस्य + ङसिँ न्यून + सुँ असँ + लट् + तिप् ।"))!!.frames.last()
        val roles = frame.relations.associate { it.participant.pada.sourceText.replace(" ", "") to it }
        assertEquals(Karaka.KARTR, (roles.getValue("न्यून+तमप्+सुँ").resolution as FrameKarakaResolution.Resolved).karaka)
        assertEquals(Karaka.ANIRDHARITA, (roles.getValue("उत्तर+सुप्").resolution as FrameKarakaResolution.Resolved).karaka)
        // 2.3.41 यतश्च निर्धारणम्: the group the member is chosen from.
        assertEquals("अकारक: निर्धारण", frame.karakaLabel(roles.getValue("उत्तर+सुप्")))
    }

    @Test fun `superlatives and prefixed absolutives render with their affixes`() {
        val sadhaka = PvmUktiSadhaka()
        assertEquals("सूच्याम् अधिकतमस्य स्थानम् अन्विच्छ ।",
            sadhaka.sadhayaLine("सूची + ङि अधिक + तमप् + ङस् स्थान + अम् अनु + इष् + लोट् + सिप् ।"))
        assertEquals("यदि उत्तरेषु न्यूनतमः सदस्याद् न्यूनः अस्ति तर्हि सत्यम् अस्ति ।",
            sadhaka.sadhayaLine("यदि उत्तर + सुप् न्यून + तमप् + सुँ सदस्य + ङसिँ न्यून + सुँ असँ + लट् + तिप् तर्हि सत्य + सुँ असँ + लट् + तिप् ।"))
    }
}
