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
    fun `test 3 1 97 AchoYatSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "जि", TermKind.DHATU, upadesha = "जि")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.BHAVA)
        )
        assertTrue(AchoYatSutra.matches(state))
        assertEquals("यत्", AchoYatSutra.apply(state).state.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `test 3 1 124 RhalorNyatSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "कृ", TermKind.DHATU, upadesha = "कृ")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.BHAVA)
        )
        assertTrue(RhalorNyatSutra.matches(state))
        assertEquals("ण्यत्", RhalorNyatSutra.apply(state).state.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `test 3 1 133 NvultrchauSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "कृ", TermKind.DHATU, upadesha = "कृ")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.KARTR_VEDANA)
        )
        assertTrue(NvultrchauSutra.matches(state))
        assertEquals("तृच्", NvultrchauSutra.apply(state).state.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `test 3 1 144 GeheKahSutra`() {
        val state = DerivationState(terms = listOf(DerivationTerm("root", "ग्रह्", TermKind.DHATU, upadesha = "ग्रह्")))
        assertTrue(GeheKahSutra.matches(state))
        assertEquals("क", GeheKahSutra.apply(state).state.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `test 3 1 145 SilpiniShvunSutra`() {
        val state = DerivationState(terms = listOf(DerivationTerm("root", "नृत्", TermKind.DHATU, upadesha = "नृत्")))
        assertTrue(SilpiniShvunSutra.matches(state))
        assertEquals("ष्वुन्", SilpiniShvunSutra.apply(state).state.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `test 3 2 3 AtoAnupasargeKahSutra`() {
        val state = DerivationState(terms = listOf(DerivationTerm("root", "दा", TermKind.DHATU, upadesha = "दा")))
        assertTrue(AtoAnupasargeKahSutra.matches(state))
        assertEquals("क", AtoAnupasargeKahSutra.apply(state).state.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `test 3 2 124 LatahSatriShanacauSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "भू", TermKind.DHATU, upadesha = "भू")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.KARTR_VEDANA, rupa = Rupa(lakara = Lakara.LAT))
        )
        assertTrue(LatahSatriShanacauSutra.matches(state))
        assertEquals("शतृ", LatahSatriShanacauSutra.apply(state).state.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `test 3 2 126 LaksanaghetvohKriyahSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "गम्", TermKind.DHATU, upadesha = "गम्")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.KARTR_VEDANA, rupa = Rupa(lakara = Lakara.LAT))
        )
        assertTrue(LaksanaghetvohKriyahSutra.matches(state))
        assertEquals("शतृ", LaksanaghetvohKriyahSutra.apply(state).state.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `test 3 2 168 SanashamsabhikshuchSutra`() {
        val state = DerivationState(terms = listOf(DerivationTerm("root", "भिक्ष्", TermKind.DHATU, upadesha = "भिक्ष्")))
        assertTrue(SanashamsabhikshuchSutra.matches(state))
        assertEquals("उच्", SanashamsabhikshuchSutra.apply(state).state.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `test 3 2 178 AnyaebhyopiDrshyateSutra`() {
        val state = DerivationState(terms = listOf(DerivationTerm("root", "दृश्", TermKind.DHATU, upadesha = "दृश्")))
        assertTrue(AnyaebhyopiDrshyateSutra.matches(state))
        assertEquals("क्विप्", AnyaebhyopiDrshyateSutra.apply(state).state.allEffectiveTerms.last().upadesha)
    }
}
