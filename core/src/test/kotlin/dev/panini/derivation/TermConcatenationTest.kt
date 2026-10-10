package dev.panini.derivation

import kotlin.test.*

class TermConcatenationTest {
    @Test fun `composition preserves annotations and suffix vowel provenance without a fake substitution`() {
        val left = DerivationTerm("stem", "रामाँऽ", TermKind.PRATIPADIKA, upadesha = "राम", createdBySutra = "fixture")
        val suffix = DerivationTerm("suffix", "भ्याँम्", TermKind.PRATYAYA, upadesha = "भ्याम्")
        val original = DerivationState(listOf(left, suffix))
        val result = original.concatenateFollowingTerm(left.id, suffix.id, "7.3.102")
        assertEquals("रामाँऽभ्याँम्", result.surface)
        assertEquals(left.id, result.terms.single().id)
        assertEquals(left.upadesha, result.terms.single().upadesha)
        assertEquals(left.createdBySutra, result.terms.single().createdBySutra)
        assertEquals(setOf("भ्याम्"), result.terms.single().sourceSuffixUpadeshas)
        assertTrue(result.substitutions.isEmpty())
        val dropped = result.droppedTerms.single()
        assertEquals(suffix.surface, dropped.originalSurfaceBeforeDrop)
        assertEquals(left.id, dropped.mergedIntoTermId)
        assertEquals(0, dropped.mergedAffixVowelFromEnd)
    }

    @Test fun `suffix locus counts phonological vowels rather than written signs`() {
        val left = DerivationTerm("stem", "क्", TermKind.PRATIPADIKA)
        val suffix = DerivationTerm("suffix", "अइ", TermKind.PRATYAYA)
        val result = DerivationState(listOf(left, suffix)).concatenateFollowingTerm("stem", "suffix", "fixture")
        assertEquals("कइ", result.surface)
        assertEquals(1, result.droppedTerms.single().mergedAffixVowelFromEnd)
    }

    @Test fun `composition rejects wrong order and incomplete it processing`() {
        val left = DerivationTerm("stem", "राम", TermKind.PRATIPADIKA)
        val suffix = DerivationTerm("suffix", "सुँ", TermKind.PRATYAYA, itProcessingPhase = ItProcessingPhase.RAW_UPADESHA)
        val state = DerivationState(listOf(left, suffix))
        assertFailsWith<IllegalArgumentException> { state.concatenateFollowingTerm("suffix", "stem", "fixture") }
        assertFailsWith<IllegalArgumentException> { state.concatenateFollowingTerm("stem", "suffix", "fixture") }
    }
}
