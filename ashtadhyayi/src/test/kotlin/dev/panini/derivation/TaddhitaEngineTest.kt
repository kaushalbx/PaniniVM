package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya5.pada1.TenaTulyamKriyaCedVatihSutra
import dev.panini.ashtadhyayi.adhyaya5.pada3.PancamyasTasilSutra
import dev.panini.ashtadhyayi.adhyaya5.pada3.SaptamyasTralSutra
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TaddhitaEngineTest {

    @Test
    fun `derives Dasarathi from Dasaratha for Apatya`() {
        val result = TaddhitaEngine().derive("दशरथ", DerivationalMeaning.APATYA)
        assertTrue(result.applications.any { it.sutra == "4.1.95" })
        assertTrue(result.applications.any { it.sutra == "7.2.117" })
        assertTrue(result.applications.any { it.sutra == "6.4.148" })
        assertEquals("दाशरथि", result.final.surface)
    }

    @Test
    fun `derives Gargya from Garga for Gotra Apatya`() {
        val result = TaddhitaEngine().derive("गर्ग", DerivationalMeaning.GOTRA)
        assertTrue(result.applications.any { it.sutra == "4.1.105" })
        assertTrue(result.applications.any { it.sutra == "7.2.117" })
        assertTrue(result.applications.any { it.sutra == "6.4.148" })
        assertEquals("गार्ग्य", result.final.surface)
    }

    @Test
    fun `derives Vatsayana from Vatsa with Phak affix`() {
        val state = DerivationState(
            terms = listOf(
                DerivationTerm("pratipadika", "वत्स", TermKind.PRATIPADIKA, upadesha = "वत्स"),
                DerivationTerm("phak", "फक्", TermKind.PRATYAYA, upadesha = "फक्")
            ),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.APATYA)
        )
        val result = DerivationEngine().derive(state)
        assertTrue(result.applications.any { it.sutra == "7.1.2" })
        assertTrue(result.applications.any { it.sutra == "7.2.118" })
        assertTrue(result.applications.any { it.sutra == "6.4.148" })
        assertEquals("वात्सायन्", result.final.surface)
    }

    @Test
    fun `derives Vainateya from Vinata with Dhak affix`() {
        val state = DerivationState(
            terms = listOf(
                DerivationTerm("pratipadika", "विनता", TermKind.PRATIPADIKA, upadesha = "विनता"),
                DerivationTerm("dhak", "ढक्", TermKind.PRATYAYA, upadesha = "ढक्")
            ),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.APATYA)
        )
        val result = DerivationEngine().derive(state)
        assertTrue(result.applications.any { it.sutra == "7.1.2" })
        assertTrue(result.applications.any { it.sutra == "7.2.118" || it.sutra == "7.2.117" })
        assertTrue(result.applications.any { it.sutra == "6.4.148" })
        assertEquals("वैनतेय्", result.final.surface)
    }

    @Test
    fun `test TenaTulyamKriyaCedVatihSutra prescribes vatih affix`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("pratipadika", "ब्राह्मण", TermKind.PRATIPADIKA, upadesha = "ब्राह्मण")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.BHAVA)
        )
        assertTrue(TenaTulyamKriyaCedVatihSutra.matches(state))
        val change = TenaTulyamKriyaCedVatihSutra.apply(state)
        val addedTerm = change.state.allEffectiveTerms.last()
        assertEquals("वत्", addedTerm.upadesha)
        assertEquals("वत्", addedTerm.surface)
    }

    @Test
    fun `test PancamyasTasilSutra prescribes tasil affix`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("pratipadika", "तद्", TermKind.PRATIPADIKA, upadesha = "तद्")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.APADANA)
        )
        assertTrue(PancamyasTasilSutra.matches(state))
        val change = PancamyasTasilSutra.apply(state)
        val addedTerm = change.state.allEffectiveTerms.last()
        assertEquals("तसिल्", addedTerm.upadesha)
        assertEquals("तस्", addedTerm.surface)
    }

    @Test
    fun `test SaptamyasTralSutra prescribes tral affix`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("pratipadika", "सर्व", TermKind.PRATIPADIKA, upadesha = "सर्व")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.TATRA_BHAVA)
        )
        assertTrue(SaptamyasTralSutra.matches(state))
        val change = SaptamyasTralSutra.apply(state)
        val addedTerm = change.state.allEffectiveTerms.last()
        assertEquals("त्रल्", addedTerm.upadesha)
        assertEquals("त्र", addedTerm.surface)
    }
}
