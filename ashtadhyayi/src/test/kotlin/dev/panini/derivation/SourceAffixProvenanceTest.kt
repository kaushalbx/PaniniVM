package dev.panini.derivation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SourceAffixProvenanceTest {
    @Test
    fun `phonological affix merger preserves its recorded upadesha`() {
        val state = DerivationState(terms = listOf(
            DerivationTerm("stem", "अ", TermKind.PRATIPADIKA, sourceSuffixUpadeshas = setOf("पूर्वप्रत्यय")),
            DerivationTerm("affix", "अ", TermKind.PRATYAYA, upadesha = "वति"),
        ))
        val merged = state.mergeTermsByVarnaSubstitution("stem", "affix", "आ", 'अ', "आ", "6.1.101")
        assertEquals(setOf("पूर्वप्रत्यय", "वति"), merged.terms.single().sourceSuffixUpadeshas)
        assertTrue(merged.droppedTerms.any { it.id == "affix" && it.upadesha == "वति" })
    }

    @Test
    fun `merging a separate stem does not promote its suffixes to the head`() {
        val state = DerivationState(terms = listOf(
            DerivationTerm("head", "अ", TermKind.PRATIPADIKA),
            DerivationTerm("member", "अ", TermKind.PRATIPADIKA, sourceSuffixUpadeshas = setOf("वति")),
        ))
        val merged = state.mergeTermsByVarnaSubstitution("head", "member", "आ", 'अ', "आ", "6.1.101")
        assertEquals(emptySet(), merged.terms.single().sourceSuffixUpadeshas)
    }
}
