package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya8.pada3.RoRiSutra
import dev.panini.ashtadhyayi.adhyaya6.pada3.DhralopePurvasyaDirghonahSutra
import kotlin.test.*

class RoRiTest {
    @Test fun `preceding an vowel can belong to the previous morphological term`() {
        for ((vowel, expected) in listOf("अँऽ" to "आँऽ", "इँऽ" to "ईँऽ", "उँऽ" to "ऊँऽ")) {
            val original = DerivationState(listOf(
                DerivationTerm("stem", vowel, TermKind.PRATIPADIKA),
                DerivationTerm("empty", "", TermKind.PRATYAYA),
                DerivationTerm("suffix", "र्", TermKind.PRATYAYA),
                DerivationTerm("next", "रथः", TermKind.PRATIPADIKA),
            ))
            val deleted = RoRiSutra.apply(original).state
            assertEquals(0, deleted.substitutions.single().sourceVarnaIndex)
            assertTrue(DhralopePurvasyaDirghonahSutra.matches(deleted))
            val result = DhralopePurvasyaDirghonahSutra.apply(deleted).state
            assertEquals(expected, result.terms.first().surface)
            assertEquals("stem", result.substitutions.last().targetId)
            assertEquals(original.terms.map { it.id }, result.terms.map { it.id })
            assertFalse(DhralopePurvasyaDirghonahSutra.matches(result))
        }
        for (surface in listOf("ऋ", "ए", "क्")) {
            val original = DerivationState(listOf(DerivationTerm("stem", surface, TermKind.PRATIPADIKA),
                DerivationTerm("suffix", "र्", TermKind.PRATYAYA), DerivationTerm("next", "रथः", TermKind.PRATIPADIKA)))
            assertFalse(DhralopePurvasyaDirghonahSutra.matches(RoRiSutra.apply(original).state))
        }
    }

    @Test fun `r lopa records the exact internal or boundary occurrence`() {
        for ((surfaces, expected) in listOf(
            listOf("निर्रक्तम्") to "नीरक्तम्",
            listOf("दुर्रक्तम्") to "दूरक्तम्",
            listOf("पुनर्", "रक्तम्") to "पुनारक्तम्",
            listOf("अग्निर्", "रथः") to "अग्नीरथः",
            listOf("इन्दुर्", "रथः") to "इन्दूरथः",
        )) {
            val original = DerivationState(surfaces.mapIndexed { index, surface ->
                DerivationTerm("term-$index", surface, TermKind.PRATIPADIKA, createdBySutra = "fixture")
            })
            assertTrue(RoRiSutra.matches(original))
            val deleted = RoRiSutra.apply(original).state
            val position = assertNotNull(deleted.substitutions.single().sourceVarnaIndex)
            assertTrue(position > 0)
            assertTrue(DhralopePurvasyaDirghonahSutra.matches(deleted))
            val result = DhralopePurvasyaDirghonahSutra.apply(deleted).state
            assertEquals(expected, result.surface)
            assertEquals(original.terms.map { it.id }, result.terms.map { it.id })
            assertEquals(original.terms.map { it.upadesha }, result.terms.map { it.upadesha })
            assertFalse(RoRiSutra.matches(result))
            assertFalse(DhralopePurvasyaDirghonahSutra.matches(result))
        }
    }

    @Test fun `r lopa preserves nasalization and signs without matching intervening vowels`() {
        val original = DerivationState(listOf(DerivationTerm("left", "पुनँर्ऽ", TermKind.PRATIPADIKA),
            DerivationTerm("right", "रक्तम्", TermKind.PRATIPADIKA)))
        val result = DhralopePurvasyaDirghonahSutra.apply(RoRiSutra.apply(original).state).state
        assertEquals("पुनाँऽ", result.terms.first().surface)
        for (surface in listOf("रर", "रिर", "र्", "रर्")) {
            assertFalse(RoRiSutra.matches(DerivationState(listOf(DerivationTerm("x", surface, TermKind.PRATIPADIKA)))))
        }
    }
}
