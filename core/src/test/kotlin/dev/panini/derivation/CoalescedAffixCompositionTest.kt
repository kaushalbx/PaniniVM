package dev.panini.derivation

import kotlin.test.*

class CoalescedAffixCompositionTest {
    @Test fun `coalesced initial affix vowel is counted even after its token is deleted`() {
        val stem = DerivationTerm("stem", "आ", TermKind.PRATIPADIKA)
        val original = DerivationTerm("suffix", "अकँऽ", TermKind.PRATYAYA)
        val state = DerivationState(listOf(stem, original))
        assertFailsWith<IllegalArgumentException> {
            state.concatenateAfterInitialVowelCoalescence(stem.id, original, "test")
        }
        val deleted = state.deleteTermVarnas(original.id, 0, 1, "test")
        val result = deleted.concatenateAfterInitialVowelCoalescence(stem.id, original, "test")
        assertEquals("आकँऽ", result.surface)
        assertEquals(original.surface, result.droppedTerms.single().originalSurfaceBeforeDrop)
        assertEquals(1, result.droppedTerms.single().mergedAffixVowelFromEnd)
        assertEquals(stem.id, result.droppedTerms.single().mergedIntoTermId)
        assertEquals(deleted.substitutions, result.substitutions)
    }
}
