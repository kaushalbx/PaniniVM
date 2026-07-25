package dev.panini.ashtadhyayi.adhyaya3

import dev.panini.ashtadhyayi.adhyaya3.pada1.DhatohKarmanahSamanakartrkadIcchayamSanSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.DhatorEkayacoHaladerKriyasamabhihareYangSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.GupTijKitsadbhyahSanSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.HetumatiCaSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.SanaadyantaDhatavahSutra
import dev.panini.derivation.DerivationEngine
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
    fun `derives san desiderative affix via GupTijKitsadbhyahSanSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "गुप्", TermKind.DHATU, upadesha = "गुप्")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.BHAVA)
        )
        val result = DerivationEngine(listOf(GupTijKitsadbhyahSanSutra)).derive(state)
        assertTrue(result.applications.any { it.sutra == "3.1.5" })
        assertEquals("सन्", result.final.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `derives san desiderative affix via DhatohKarmanahSamanakartrkadIcchayamSanSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "भू", TermKind.DHATU, upadesha = "भू")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.BHAVA)
        )
        val result = DerivationEngine(listOf(DhatohKarmanahSamanakartrkadIcchayamSanSutra)).derive(state)
        assertTrue(result.applications.any { it.sutra == "3.1.7" })
        assertEquals("सन्", result.final.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `derives yaṅ frequentative affix via DhatorEkayacoHaladerKriyasamabhihareYangSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "पच्", TermKind.DHATU, upadesha = "पच्")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.BHAVA)
        )
        val result = DerivationEngine(listOf(DhatorEkayacoHaladerKriyasamabhihareYangSutra)).derive(state)
        assertTrue(result.applications.any { it.sutra == "3.1.22" })
        assertEquals("यङ्", result.final.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `derives ṇic causative affix via HetumatiCaSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "कृ", TermKind.DHATU, upadesha = "कृ")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.BHAVA)
        )
        val result = DerivationEngine(listOf(HetumatiCaSutra)).derive(state)
        assertTrue(result.applications.any { it.sutra == "3.1.26" })
        assertEquals("णिच्", result.final.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `assigns dhātu saṃjñā via SanaadyantaDhatavahSutra`() {
        val state = DerivationState(
            terms = listOf(
                DerivationTerm("root", "भू", TermKind.DHATU, upadesha = "भू"),
                DerivationTerm("san", "स", TermKind.PRATYAYA, upadesha = "सन्")
            )
        )
        val result = DerivationEngine(listOf(SanaadyantaDhatavahSutra)).derive(state)
        assertTrue(result.applications.any { it.sutra == "3.1.32" })
    }
}
