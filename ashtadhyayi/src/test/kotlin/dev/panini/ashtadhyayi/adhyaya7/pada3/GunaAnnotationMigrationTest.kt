package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.core.ItMarker
import dev.panini.derivation.*
import dev.panini.shiksha.Samjna
import kotlin.test.*

class GunaAnnotationMigrationTest {
    private fun state(surface: String, affix: DerivationTerm, samjna: Samjna? = null): DerivationState {
        val stem = DerivationTerm("stem", surface, TermKind.DHATU, upadesha = "source", createdBySutra = "fixture")
        return DerivationState(listOf(stem, affix), activeAdhikaras = setOf("6.4.1"),
            samjnas = samjna?.let { setOf(SamjnaAssignment(if (it == Samjna.SAMBUDDHI) affix.id else stem.id, it)) } ?: emptySet())
    }

    private fun check(original: DerivationState, expected: String, index: Int, result: DerivationState) {
        assertEquals(expected, result.terms.first().surface)
        assertEquals(original.terms.first().id, result.terms.first().id)
        assertEquals(original.terms.first().upadesha, result.terms.first().upadesha)
        assertEquals(original.terms.first().createdBySutra, result.terms.first().createdBySutra)
        assertEquals(original.terms.last(), result.terms.last())
        assertEquals(original.samjnas, result.samjnas)
        assertEquals(index, result.substitutions.single().sourceVarnaIndex)
    }

    @Test fun `final guna preserves vowel nasality and written signs`() {
        val sambuddhi = state("हरिँऽ", DerivationTerm("su", "स्", TermKind.PRATYAYA), Samjna.SAMBUDDHI)
        assertTrue(HrasvasyaGunaSutra.matches(sambuddhi))
        check(sambuddhi, "हरेँऽ", 3, HrasvasyaGunaSutra.apply(sambuddhi).state)

        val ngit = state("गुरुँऽ", DerivationTerm("ngi", "इ", TermKind.PRATYAYA, itMarkers = setOf(ItMarker.NGIT)), Samjna.GHI)
        assertTrue(GherNitiSutra.matches(ngit))
        check(ngit, "गुरोँऽ", 3, GherNitiSutra.apply(ngit).state)

        val jas = state("हरिँऽ", DerivationTerm("jas", "अँस्", TermKind.PRATYAYA, upadesha = "जस्"), Samjna.GHI)
        assertTrue(JasiCaSutra.matches(jas))
        check(jas, "हरेँऽ", 3, JasiCaSutra.apply(jas).state)
        assertFalse(JasiCaSutra.matches(jas.copy(terms = listOf(jas.terms.first(), jas.terms.last().copy(kind = TermKind.DHATU)))))
        assertFalse(JasiCaSutra.matches(jas.copy(terms = listOf(jas.terms.first(), jas.terms.last().copy(upadesha = "शि")))))
    }

    @Test fun `r guna expansion maps nasalization to its vowel and preserves sign`() {
        val original = state("पितृँऽ", DerivationTerm("sup-ngi", "इ", TermKind.PRATYAYA, upadesha = "ङि"))
        assertTrue(RtoNgiSarvanamasthanayohSutra.matches(original))
        check(original, "पितँर्ऽ", 3, RtoNgiSarvanamasthanayohSutra.apply(original).state)
    }

    @Test fun `upadha guna changes only its exact occurrence`() {
        val original = state("बुँध्ऽ", DerivationTerm("suffix", "अ", TermKind.PRATYAYA))
        // Applicability is covered by the engine tests; this exercises mutation independently.
        check(original, "बोँध्ऽ", 1, PugantalaghupadhasyaCaSutra.apply(original).state)
    }
}
