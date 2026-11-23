package dev.panini.ashtadhyayi

import dev.panini.ashtadhyayi.adhyaya5.pada3.EdhaccaSutra
import dev.panini.ashtadhyayi.adhyaya5.pada3.SankhyayascavidhartheDhaSutra
import dev.panini.ashtadhyayi.adhyaya5.pada4.DvitrichaturbhyahSucSutra
import dev.panini.ashtadhyayi.adhyaya5.pada4.SankhyayahKriyaAbhyavrttiKrtvasucSutra
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

import dev.panini.derivation.SamjnaAssignment
import dev.panini.shiksha.Samjna

class NumeralTaddhitaTest {

    @Test
    fun `test SankhyayascavidhartheDhaSutra derives dvidha and pancadha`() {
        val state = DerivationState(
            terms = listOf(
                DerivationTerm(id = "s1", surface = "द्वि", kind = TermKind.PRATIPADIKA, upadesha = "द्वि")
            ),
            samjnas = setOf(SamjnaAssignment("s1", Samjna.TADDHITA))
        )
        assertTrue(SankhyayascavidhartheDhaSutra.matches(state))
        val change = SankhyayascavidhartheDhaSutra.apply(state)
        assertEquals("धा", change.state.terms.last().surface)
    }

    @Test
    fun `test EdhaccaSutra matches eka and derives ekadha`() {
        val state = DerivationState(
            terms = listOf(
                DerivationTerm(id = "s1", surface = "एक", kind = TermKind.PRATIPADIKA, upadesha = "एक")
            ),
            samjnas = setOf(SamjnaAssignment("s1", Samjna.TADDHITA))
        )
        assertTrue(EdhaccaSutra.matches(state))
        val change = EdhaccaSutra.apply(state)
        assertEquals("धा", change.state.terms.last().surface)
    }

    @Test
    fun `test SankhyayahKriyaAbhyavrttiKrtvasucSutra derives pancakrtvah`() {
        val state = DerivationState(
            terms = listOf(
                DerivationTerm(id = "s1", surface = "पञ्च", kind = TermKind.PRATIPADIKA, upadesha = "पञ्चन्")
            ),
            samjnas = setOf(SamjnaAssignment("s1", Samjna.TADDHITA))
        )
        assertTrue(SankhyayahKriyaAbhyavrttiKrtvasucSutra.matches(state))
        val change = SankhyayahKriyaAbhyavrttiKrtvasucSutra.apply(state)
        assertEquals("कृत्वस्च्", change.state.terms.last().surface)
        assertEquals(dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA, change.state.terms.last().itProcessingPhase)
    }

    @Test
    fun `5_4_18 replaces krtvasuc with suc after dvi tri catur, and 5_4_19 gives sakrt for eka`() {
        fun frequency(stem: String) = DerivationState(
            terms = listOf(DerivationTerm(id = "s1", surface = stem, kind = TermKind.PRATIPADIKA, upadesha = stem)),
            samjnas = setOf(SamjnaAssignment("s1", Samjna.KRTVASUC)),
        )
        for (stem in listOf("द्वि", "त्रि", "चतुर्")) {
            val state = frequency(stem)
            assertTrue(DvitrichaturbhyahSucSutra.matches(state), stem)
            val result = DvitrichaturbhyahSucSutra.apply(state).state
            assertEquals(listOf(stem, "सुच्"), result.terms.map { it.upadesha }, stem)
            assertTrue(!DvitrichaturbhyahSucSutra.matches(result), "सुच् is added once")
        }
        // 5.4.18 is an apavāda of 5.4.17 only for these three; पञ्चन् keeps कृत्वसुच्.
        assertTrue(!DvitrichaturbhyahSucSutra.matches(frequency("पञ्चन्")))
        assertTrue(!DvitrichaturbhyahSucSutra.matches(frequency("एक")))

        val eka = frequency("एक")
        assertTrue(dev.panini.ashtadhyayi.adhyaya5.pada4.EkasyaSakrcCaSutra.matches(eka))
        val sakrt = dev.panini.ashtadhyayi.adhyaya5.pada4.EkasyaSakrcCaSutra.apply(eka).state
        assertEquals("सकृत्", sakrt.terms.first().surface)
        assertEquals("सुच्", sakrt.terms.last().upadesha)
    }
}
