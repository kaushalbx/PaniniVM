package dev.panini.ashtadhyayi.adhyaya3

import dev.panini.ashtadhyayi.adhyaya3.pada1.AchoYatSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.GeheKahSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.NvultrchauSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.RhalorNyatSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.SilpiniShvunSutra
import dev.panini.ashtadhyayi.adhyaya3.pada2.AnyaebhyopiDrshyateSutra
import dev.panini.ashtadhyayi.adhyaya3.pada2.AtoAnupasargeKahSutra
import dev.panini.ashtadhyayi.adhyaya3.pada2.LaksanaghetvohKriyahSutra
import dev.panini.ashtadhyayi.adhyaya3.pada2.LatahSatriShanacauSutra
import dev.panini.ashtadhyayi.adhyaya3.pada2.SanashamsabhikshuchSutra
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

class Batch10KrtSutrasTest {

    @Test
    fun `derives yat affix via AchoYatSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "जि", TermKind.DHATU, upadesha = "जि")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.BHAVA)
        )
        val result = DerivationEngine(listOf(AchoYatSutra)).derive(state)
        assertTrue(result.applications.any { it.sutra == "3.1.97" })
        assertEquals("यत्", result.final.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `derives nyat affix via RhalorNyatSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "कृ", TermKind.DHATU, upadesha = "कृ")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.BHAVA)
        )
        val result = DerivationEngine(listOf(RhalorNyatSutra)).derive(state)
        assertTrue(result.applications.any { it.sutra == "3.1.124" })
        assertEquals("ण्यत्", result.final.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `derives trc affix via NvultrchauSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "कृ", TermKind.DHATU, upadesha = "कृ")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.KARTR_VEDANA)
        )
        val result = DerivationEngine(listOf(NvultrchauSutra)).derive(state)
        assertTrue(result.applications.any { it.sutra == "3.1.133" })
        assertEquals("तृच्", result.final.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `derives ka affix via GeheKahSutra`() {
        val state = DerivationState(terms = listOf(DerivationTerm("root", "ग्रह्", TermKind.DHATU, upadesha = "ग्रह्")))
        val result = DerivationEngine(listOf(GeheKahSutra)).derive(state)
        assertTrue(result.applications.any { it.sutra == "3.1.144" })
        assertEquals("क", result.final.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `derives ṣvun affix via SilpiniShvunSutra`() {
        val state = DerivationState(terms = listOf(DerivationTerm("root", "नृत्", TermKind.DHATU, upadesha = "नृत्")))
        val result = DerivationEngine(listOf(SilpiniShvunSutra)).derive(state)
        assertTrue(result.applications.any { it.sutra == "3.1.145" })
        assertEquals("ष्वुन्", result.final.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `derives ka affix via AtoAnupasargeKahSutra`() {
        val state = DerivationState(terms = listOf(DerivationTerm("root", "दा", TermKind.DHATU, upadesha = "दा")))
        val result = DerivationEngine(listOf(AtoAnupasargeKahSutra)).derive(state)
        assertTrue(result.applications.any { it.sutra == "3.2.3" })
        assertEquals("क", result.final.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `derives śatṛ affix via LatahSatriShanacauSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "भू", TermKind.DHATU, upadesha = "भू")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.KARTR_VEDANA, rupa = Rupa(lakara = Lakara.LAT))
        )
        val result = DerivationEngine(listOf(LatahSatriShanacauSutra)).derive(state)
        assertTrue(result.applications.any { it.sutra == "3.2.124" })
        assertEquals("शतृँ", result.final.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `derives śatṛ affix via LaksanaghetvohKriyahSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "गम्", TermKind.DHATU, upadesha = "गम्")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.KARTR_VEDANA, rupa = Rupa(lakara = Lakara.LAT))
        )
        val result = DerivationEngine(listOf(LaksanaghetvohKriyahSutra)).derive(state)
        assertTrue(result.applications.any { it.sutra == "3.2.126" })
        assertEquals("शतृँ", result.final.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `derives uc affix via SanashamsabhikshuchSutra`() {
        val state = DerivationState(terms = listOf(DerivationTerm("root", "भिक्ष्", TermKind.DHATU, upadesha = "भिक्ष्")))
        val result = DerivationEngine(listOf(SanashamsabhikshuchSutra)).derive(state)
        assertTrue(result.applications.any { it.sutra == "3.2.168" })
        assertEquals("उच्", result.final.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `derives kvip affix via AnyaebhyopiDrshyateSutra`() {
        val state = DerivationState(terms = listOf(DerivationTerm("root", "दृश्", TermKind.DHATU, upadesha = "दृश्")))
        val result = DerivationEngine(listOf(AnyaebhyopiDrshyateSutra)).derive(state)
        assertTrue(result.applications.any { it.sutra == "3.2.178" })
        assertEquals("क्विप्", result.final.allEffectiveTerms.last().upadesha)
    }
}
