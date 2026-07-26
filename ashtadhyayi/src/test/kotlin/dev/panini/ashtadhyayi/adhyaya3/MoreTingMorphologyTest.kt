package dev.panini.ashtadhyayi.adhyaya3

import dev.panini.ashtadhyayi.adhyaya3.pada1.GupDhoopVichchhiPaniPanibhyOyaSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.KamerNingSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.RtIyIyAnehKyanSutra
import dev.panini.ashtadhyayi.adhyaya3.pada4.LanSakatayanasyaIvaSutra
import dev.panini.core.Lakara
import dev.panini.derivation.DerivationEngine
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
    fun `derives āya affix via GupDhoopVichchhiPaniPanibhyOyaSutra`() {
        val state1 = DerivationState(
            terms = listOf(DerivationTerm("root", "गुप्", TermKind.DHATU, upadesha = "गुप्")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.BHAVA)
        )
        val state2 = DerivationState(
            terms = listOf(DerivationTerm("root", "धूप", TermKind.DHATU, upadesha = "धूप")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.BHAVA)
        )
        val result1 = DerivationEngine(listOf(GupDhoopVichchhiPaniPanibhyOyaSutra)).derive(state1)
        val result2 = DerivationEngine(listOf(GupDhoopVichchhiPaniPanibhyOyaSutra)).derive(state2)
        assertTrue(result1.applications.any { it.sutra == "3.1.28" })
        assertTrue(result2.applications.any { it.sutra == "3.1.28" })
        assertEquals("आय", result1.final.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `derives kyaṅ affix via RtIyIyAnehKyanSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "ऋतीय", TermKind.DHATU, upadesha = "ऋतीय")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.BHAVA)
        )
        val result = DerivationEngine(listOf(RtIyIyAnehKyanSutra)).derive(state)
        assertTrue(result.applications.any { it.sutra == "3.1.29" })
        assertEquals("क्यङ्", result.final.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `derives ṇiṅ affix via KamerNingSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "कम्", TermKind.DHATU, upadesha = "कम्")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.BHAVA)
        )
        val result = DerivationEngine(listOf(KamerNingSutra)).derive(state)
        assertTrue(result.applications.any { it.sutra == "3.1.30" })
        assertEquals("णिङ्", result.final.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `substitutes jus for jhi in Laṅ via LanSakatayanasyaIvaSutra`() {
        val state = DerivationState(
            terms = listOf(
                DerivationTerm("root", "भू", TermKind.DHATU, upadesha = "भू"),
                DerivationTerm("ting", "झि", TermKind.PRATYAYA, upadesha = "झि")
            ),
            context = DerivationalContext(rupa = Rupa(lakara = Lakara.LANG))
        ).activateAdhikara("3.4.111")
        val result = DerivationEngine(listOf(LanSakatayanasyaIvaSutra)).derive(state)
        assertTrue(result.applications.any { it.sutra == "3.4.111" })
        assertEquals("जुस्", result.final.allEffectiveTerms.last().upadesha)
    }
}
