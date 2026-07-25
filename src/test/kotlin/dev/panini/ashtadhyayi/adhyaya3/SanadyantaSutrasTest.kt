package dev.panini.ashtadhyayi.adhyaya3

import dev.panini.ashtadhyayi.adhyaya3.pada1.DhatohKarmanahSamanakartrkadIcchayamSanSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.DhatorEkayacoHaladerKriyasamabhihareYangSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.GupTijKitsadbhyahSanSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.HetumatiCaSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.SanaadyantaDhatavahSutra
import dev.panini.derivation.DerivationalContext
import dev.panini.derivation.DerivationalMeaning
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SanadyantaSutrasTest {

    @Test
    fun `test 3 1 5 GupTijKitsadbhyahSanSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "गुप्", TermKind.DHATU, upadesha = "गुप्")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.BHAVA)
        )
        assertTrue(GupTijKitsadbhyahSanSutra.matches(state))
        assertEquals("सन्", GupTijKitsadbhyahSanSutra.apply(state).state.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `test 3 1 7 DhatohKarmanahSamanakartrkadIcchayamSanSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "भू", TermKind.DHATU, upadesha = "भू")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.BHAVA)
        )
        assertTrue(DhatohKarmanahSamanakartrkadIcchayamSanSutra.matches(state))
        assertEquals("सन्", DhatohKarmanahSamanakartrkadIcchayamSanSutra.apply(state).state.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `test 3 1 22 DhatorEkayacoHaladerKriyasamabhihareYangSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "पच्", TermKind.DHATU, upadesha = "पच्")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.BHAVA)
        )
        assertTrue(DhatorEkayacoHaladerKriyasamabhihareYangSutra.matches(state))
        assertEquals("यङ्", DhatorEkayacoHaladerKriyasamabhihareYangSutra.apply(state).state.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `test 3 1 26 HetumatiCaSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "कृ", TermKind.DHATU, upadesha = "कृ")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.BHAVA)
        )
        assertTrue(HetumatiCaSutra.matches(state))
        assertEquals("णिच्", HetumatiCaSutra.apply(state).state.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `test 3 1 32 SanaadyantaDhatavahSutra`() {
        val state1 = DerivationState(
            terms = listOf(
                DerivationTerm("root", "भू", TermKind.DHATU, upadesha = "भू"),
                DerivationTerm("san", "स", TermKind.PRATYAYA, upadesha = "सन्")
            )
        )
        assertTrue(SanaadyantaDhatavahSutra.matches(state1))
    }
}
