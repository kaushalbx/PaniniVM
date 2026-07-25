package dev.panini.ashtadhyayi.adhyaya3

import dev.panini.ashtadhyayi.adhyaya3.pada1.GupDhoopVichchhiPaniPanibhyOyaSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.KamerNingSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.RtIyIyAnehKyanSutra
import dev.panini.ashtadhyayi.adhyaya3.pada4.LanSakatayanasyaIvaSutra
import dev.panini.core.Lakara
import dev.panini.derivation.DerivationalContext
import dev.panini.derivation.DerivationalMeaning
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.Rupa
import dev.panini.derivation.TermKind
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MoreTingMorphologyTest {

    @Test
    fun `test 3 1 28 GupDhoopVichchhiPaniPanibhyOyaSutra`() {
        val state1 = DerivationState(
            terms = listOf(DerivationTerm("root", "गुप्", TermKind.DHATU, upadesha = "गुप्")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.BHAVA)
        )
        val state2 = DerivationState(
            terms = listOf(DerivationTerm("root", "धूप", TermKind.DHATU, upadesha = "धूप")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.BHAVA)
        )
        assertTrue(GupDhoopVichchhiPaniPanibhyOyaSutra.matches(state1))
        assertTrue(GupDhoopVichchhiPaniPanibhyOyaSutra.matches(state2))
        assertEquals("आय", GupDhoopVichchhiPaniPanibhyOyaSutra.apply(state1).state.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `test 3 1 29 RtIyIyAnehKyanSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "ऋतीय", TermKind.DHATU, upadesha = "ऋतीय")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.BHAVA)
        )
        assertTrue(RtIyIyAnehKyanSutra.matches(state))
        assertEquals("क्यङ्", RtIyIyAnehKyanSutra.apply(state).state.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `test 3 1 30 KamerNingSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "कम्", TermKind.DHATU, upadesha = "कम्")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.BHAVA)
        )
        assertTrue(KamerNingSutra.matches(state))
        assertEquals("णिङ्", KamerNingSutra.apply(state).state.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `test 3 4 111 LanSakatayanasyaIvaSutra`() {
        val state = DerivationState(
            terms = listOf(
                DerivationTerm("root", "भू", TermKind.DHATU, upadesha = "भू"),
                DerivationTerm("ting", "झि", TermKind.PRATYAYA, upadesha = "झि")
            ),
            context = DerivationalContext(rupa = Rupa(lakara = Lakara.LANG))
        ).activateAdhikara("3.4.111")
        assertTrue(LanSakatayanasyaIvaSutra.matches(state))
        assertEquals("जुस्", LanSakatayanasyaIvaSutra.apply(state).state.allEffectiveTerms.last().upadesha)
    }
}
