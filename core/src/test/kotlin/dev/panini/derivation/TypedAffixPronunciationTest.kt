package dev.panini.derivation

import dev.panini.core.*
import kotlin.test.*

class TypedAffixPronunciationTest {
    @Test fun `generic typed construction and prescribing factories project identical operative material`() {
        val declarations: List<TypedAffix> = listOf(KrtAffix.KVIP) + KitAugment.entries + MitAugment.entries + FrequencyAffix.entries
        for (affix in declarations) {
            val term = affix.term("term")
            val prescribed = when (affix) {
                is KrtAffix -> affix.rawTerm("term", "test-prescriber")
                is KitAugment -> affix.rawTerm("term", "test-prescriber")
                is MitAugment -> affix.rawTerm("term", "test-prescriber")
                is FrequencyAffix -> affix.rawTerm("term", "test-prescriber")
                else -> error("Unexpected declaration")
            }
            assertEquals(prescribed.varnas, term.varnas, affix.upadesha)
            assertEquals(prescribed.nonOperativeUpadeshaSegments, term.nonOperativeUpadeshaSegments)
            assertEquals(prescribed.kind, term.kind)
            assertEquals(affix.upadesha, term.upadesha)
            assertNull(term.createdBySutra)
            assertEquals(ItProcessingPhase.RAW_UPADESHA, term.itProcessingPhase)
        }
    }

    @Test fun `ordinary affix identity and initial form remain unchanged`() {
        for (affix in KrtAffix.entries.filter { it.pronunciationOnlyVarnaIndices.isEmpty() }) {
            val term = affix.term("term")
            assertEquals(affix.initialSurface, term.surface)
            assertEquals(affix.upadesha, term.upadesha)
            assertTrue(term.nonOperativeUpadeshaSegments.isEmpty())
        }
    }
}
