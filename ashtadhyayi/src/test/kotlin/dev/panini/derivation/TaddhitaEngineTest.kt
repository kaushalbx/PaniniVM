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
        val result = DerivationEngine(dev.panini.ashtadhyayi.Ashtadhyayi.executableSutras).derive(state)
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
        val result = DerivationEngine(dev.panini.ashtadhyayi.Ashtadhyayi.executableSutras).derive(state)
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
        assertEquals("त्रल्", addedTerm.surface)
        assertEquals(ItProcessingPhase.RAW_UPADESHA, addedTerm.itProcessingPhase)
    }

    @Test
    fun `matup derives rupavat and dhimat with mator vah`() {
        val res1 = TaddhitaEngine().derive("रूप", dev.panini.shiksha.Samjna.MATUP)
        assertEquals("रूपवत्", res1.final.surface)
        assertTrue(res1.applications.any { it.sutra == "5.2.94" })
        assertTrue(res1.applications.any { it.sutra == "8.2.9" })

        val res2 = TaddhitaEngine().derive("धी", dev.panini.shiksha.Samjna.MATUP)
        assertEquals("धीमत्", res2.final.surface)
        assertTrue(res2.applications.any { it.sutra == "5.2.94" })
    }

    @Test
    fun `tvatalau derives gurutva and guruta`() {
        val res1 = TaddhitaEngine().derive("गुरु", dev.panini.shiksha.Samjna.TVA)
        assertEquals("गुरुत्व", res1.final.surface)
        assertTrue(res1.applications.any { it.sutra == "5.1.119" })

        val res2 = TaddhitaEngine().derive("गुरु", dev.panini.shiksha.Samjna.TAL)
        assertEquals("गुरुता", res2.final.surface)
        assertTrue(res2.applications.any { it.sutra == "5.1.119" })
    }

    @Test
    fun `tarap and tamap derive patutara and patutama`() {
        val res1 = TaddhitaEngine().derive("पटु", dev.panini.shiksha.Samjna.TARAP)
        assertEquals("पटुतर", res1.final.surface)
        assertTrue(res1.applications.any { it.sutra == "5.3.55" })

        val res2 = TaddhitaEngine().derive("पटु", dev.panini.shiksha.Samjna.TAMAP)
        assertEquals("पटुतम", res2.final.surface)
        assertTrue(res2.applications.any { it.sutra == "5.3.57" })
    }

    @Test
    fun `derivePatronymic derives Vasudevah Dasarathih and Gargyah`() {
        val res1 = TaddhitaEngine().derivePatronymic("वसुदेव", dev.panini.shiksha.Samjna.AN_PRATYAYA)
        assertEquals("वासुदेवः", res1.final.surface)
        assertTrue(res1.applications.any { it.sutra == "4.1.92" })

        val res2 = TaddhitaEngine().derivePatronymic("दशरथ", dev.panini.shiksha.Samjna.IN_PRATYAYA)
        assertEquals("दाशरथिः", res2.final.surface)

        val res3 = TaddhitaEngine().derivePatronymic("गर्ग", dev.panini.shiksha.Samjna.YAN_PRATYAYA)
        assertEquals("गार्ग्यः", res3.final.surface)
    }
}
