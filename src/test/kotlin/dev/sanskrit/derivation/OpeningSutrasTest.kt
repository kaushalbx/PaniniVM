package dev.sanskrit.derivation

import dev.sanskrit.ashtadhyayi.adhyaya1.pada1.HaloAnantarahSamyogahSutra
import dev.sanskrit.ashtadhyayi.adhyaya1.pada1.IkoGunaVrddhiSutra
import dev.sanskrit.ashtadhyayi.adhyaya1.pada1.KngitiCaSutra
import dev.sanskrit.ashtadhyayi.adhyaya1.pada1.NajjhalauSutra
import dev.sanskrit.ashtadhyayi.adhyaya1.pada1.TulyasyaprayatnamSavarnamSutra
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class OpeningSutrasTest {
    @Test
    fun `iko guna vrddhi performs the requested substitution and records it`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "इ", TermKind.DHATU)),
            semanticFeatures = setOf(SemanticFeature.GUNA_REQUEST),
        )

        val change = IkoGunaVrddhiSutra.apply(state)

        assertEquals("ए", change.state.terms.single().surface)
        assertEquals("ए", change.state.substitutions.single().replacement)
        assertEquals("1.1.3", change.state.substitutions.single().sutra)
    }

    @Test
    fun `kit affix blocks iko guna vrddhi`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("suffix", "क्त", TermKind.PRATYAYA, itMarkers = setOf(ItMarker.KIT))),
        )

        val change = KngitiCaSutra.apply(state)

        assertEquals("1.1.5", change.state.blockedSutras["1.1.3"])
    }

    @Test
    fun `samyoga recognises a consonant cluster`() {
        val state = DerivationState(listOf(DerivationTerm("stem", "क्त", TermKind.PRATIPADIKA)))

        val change = HaloAnantarahSamyogahSutra.apply(state)

        assertTrue(SamjnaAssignment("stem", Samjna.SAMYOGA) in change.state.samjnas)
    }

    @Test
    fun `savarna designates matching classes but rejects vowel consonant comparison`() {
        val matching = VarnaComparison("left", "right", 'अ', 'आ', true, true, true)
        val unlike = VarnaComparison("vowel", "consonant", 'अ', 'क', true, true, false)
        val state = DerivationState(
            terms = listOf(DerivationTerm("left", "अ", TermKind.PRATIPADIKA)),
            varnaComparisons = setOf(matching, unlike),
        )

        val savarna = TulyasyaprayatnamSavarnamSutra.apply(state)
        val prohibition = NajjhalauSutra.apply(savarna.state)

        assertTrue(SamjnaAssignment("left", Samjna.SAVARNA) in savarna.state.samjnas)
        assertTrue(prohibition.state.varnaComparisons.single { it == unlike.copy(forbidden = true) }.forbidden)
    }
}
