package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya1.pada3.TasyaLopahSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.HalantyamSutra
import dev.panini.ashtadhyayi.adhyaya1.pada3.UpadesheAjanunasikaItSutra
import dev.panini.core.ItMarker
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NasalizedVarnaDesignationTest {
    @Test
    fun `final consonant designation uses its token position after a nasalized vowel`() {
        val term = DerivationTerm("augment", "नुँट्", TermKind.AGAMA,
            itProcessingPhase = ItProcessingPhase.RAW_UPADESHA)
        val nasal = UpadesheAjanunasikaItSutra.apply(DerivationState(listOf(term))).state
        assertTrue(HalantyamSutra.matches(nasal))
        val final = HalantyamSutra.apply(nasal).state
        assertEquals(listOf(setOf(1), setOf(2)), final.terms.single().itDesignations.map { it.varnaIndices })
        assertEquals(listOf("ुँ", "ट्"), final.terms.single().itDesignations.map { it.designatedText })
        assertFalse(HalantyamSutra.matches(final))
        assertEquals("न्", TasyaLopahSutra.apply(final).state.surface)
    }

    @Test
    fun `nasalized independent dependent and inherent vowels are selected as tokens`() {
        for ((written, remaining, provenance) in listOf(
            Triple("उँ", "", "उँ"),
            Triple("तुँ", "त्", "ुँ"),
            Triple("कँ", "क्", "ँ"),
            Triple("उँतुँ", "त्", "उँ,ुँ"),
        )) {
            val term = DerivationTerm("affix", written, TermKind.PRATYAYA,
                itProcessingPhase = ItProcessingPhase.RAW_UPADESHA)
            val state = DerivationState(listOf(term))
            assertTrue(UpadesheAjanunasikaItSutra.matches(state), written)
            val designated = UpadesheAjanunasikaItSutra.apply(state).state
            assertFalse(UpadesheAjanunasikaItSutra.matches(designated), written)
            assertEquals(provenance, designated.terms.single().itDesignations.joinToString(",") { it.designatedText })
            val processed = TasyaLopahSutra.apply(designated).state
            assertEquals(remaining, processed.surface, written)
            assertEquals(term.id, processed.terms.single().id)
            assertEquals(term.upadesha, processed.terms.single().upadesha)
            assertEquals(provenance, processed.terms.single().itMarkerProvenance.joinToString(",") { it.designatedText })
            assertTrue(processed.terms.single().hasEffectiveMarker(ItMarker.U))
            processed.requireCompleteItProcessing()
        }
    }

    @Test
    fun `ordinary vowels and processed roots do not acquire nasal vowel it status`() {
        for (term in listOf(
            DerivationTerm("affix", "तु", TermKind.PRATYAYA, itProcessingPhase = ItProcessingPhase.RAW_UPADESHA),
            DerivationTerm("root", "कँ", TermKind.DHATU, itProcessingPhase = ItProcessingPhase.RAW_UPADESHA),
        )) assertFalse(UpadesheAjanunasikaItSutra.matches(DerivationState(listOf(term))))
    }
}
