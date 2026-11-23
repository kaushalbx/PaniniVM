package dev.panini.sankhya

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/** One-word compound cardinals are recognized by their forward derivation, only to be rewritten segmented. */
class DerivedCompoundCardinalTest {
    @Test fun `derived one-word compounds have a segmented spelling`() {
        val expected = mapOf(
            "एकादशन्" to "एक + दशन्", "द्वादशन्" to "द्वि + दशन्", "त्रयोदशन्" to "त्रि + दशन्",
            "षोडशन्" to "षष् + दशन्", "अष्टादशन्" to "अष्टन् + दशन्", "नवदशन्" to "नवन् + दशन्",
            "एकविंशति" to "एक + विंशति", "पञ्चविंशति" to "पञ्चन् + विंशति", "त्रयस्त्रिंशत्" to "त्रि + त्रिंशत्",
            "नवनवति" to "नवन् + नवति",
        )
        expected.forEach { (stem, segmented) -> assertEquals(segmented, DerivedCompoundCardinal.segmented(stem), stem) }
    }

    @Test fun `primitive and underived spellings have none`() {
        assertNull(DerivedCompoundCardinal.segmented("दशन्"), "primitive")
        assertNull(DerivedCompoundCardinal.segmented("विंशति"), "primitive")
        assertNull(DerivedCompoundCardinal.segmented("एकादश"), "inflected surface, not the stem")
        assertNull(DerivedCompoundCardinal.segmented("एकदशन्"), "no derivation produces it")
        assertNull(DerivedCompoundCardinal.segmented("राम"))
    }

    @Test fun `each segmented spelling derives the compound back`() {
        val evaluator = SankhyaEvaluator()
        val generator = SankhyaGenerator()
        for (stem in listOf("एकादशन्", "त्रयोदशन्", "षोडशन्", "अष्टादशन्", "पञ्चविंशति", "त्रयस्त्रिंशत्")) {
            val stems = requireNotNull(DerivedCompoundCardinal.segmented(stem)).split(" + ")
            assertEquals(stem, generator.pratipadika(evaluator.evaluateStems(stems)), stem)
        }
    }
}
