package dev.panini.ashtadhyayi.adhyaya3

import dev.panini.ashtadhyayi.adhyaya3.pada3.BhaveSutra
import dev.panini.ashtadhyayi.adhyaya3.pada3.EchaIgGhanSutra
import dev.panini.ashtadhyayi.adhyaya3.pada3.HetuhetumatorLingSutra
import dev.panini.ashtadhyayi.adhyaya3.pada3.KalaSamayaVelashuTumunSutra
import dev.panini.ashtadhyayi.adhyaya3.pada3.NapumsakeBhaveKtahSutra
import dev.panini.ashtadhyayi.adhyaya3.pada3.RadorApsutra
import dev.panini.ashtadhyayi.adhyaya3.pada3.StriyamKtinSutra
import dev.panini.ashtadhyayi.adhyaya3.pada4.KartariKrtSutra
import dev.panini.ashtadhyayi.adhyaya3.pada4.SamanakartrkayohPurvakaleSutra
import dev.panini.ashtadhyayi.adhyaya3.pada4.ShakaDhrshJnAGlaGhatRabhabhLabhaprakramitumunSutra
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

class Batch10GerundInfinitiveMoodSutrasTest {

    @Test
    fun `derives ghañ affix for bhāva via BhaveSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "पच्", TermKind.DHATU, upadesha = "पच्")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.BHAVA)
        )
        assertTrue(BhaveSutra.matches(state))
        assertEquals("घञ्", BhaveSutra.apply(state).state.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `substitutes ik for ec vowel before ghañ via EchaIgGhanSutra`() {
        val state = DerivationState(
            terms = listOf(
                DerivationTerm("root", "धै", TermKind.DHATU, upadesha = "धै"),
                DerivationTerm("ghanj", "अ", TermKind.PRATYAYA, upadesha = "घञ्")
            )
        )
        assertTrue(EchaIgGhanSutra.matches(state))
        assertEquals("धि", EchaIgGhanSutra.apply(state).state.allEffectiveTerms.first().surface)
    }

    @Test
    fun `derives ap affix via RadorApsutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "कृ", TermKind.DHATU, upadesha = "कृ")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.BHAVA)
        )
        assertTrue(RadorApsutra.matches(state))
        assertEquals("अप्", RadorApsutra.apply(state).state.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `derives ktin affix via StriyamKtinSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "कृ", TermKind.DHATU, upadesha = "कृ")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.BHAVA)
        )
        assertTrue(StriyamKtinSutra.matches(state))
        assertEquals("क्तिन्", StriyamKtinSutra.apply(state).state.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `derives kta affix via NapumsakeBhaveKtahSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "हस्", TermKind.DHATU, upadesha = "हस्")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.BHAVA)
        )
        assertTrue(NapumsakeBhaveKtahSutra.matches(state))
        assertEquals("क्त", NapumsakeBhaveKtahSutra.apply(state).state.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `activates liṅ mood via HetuhetumatorLingSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "भू", TermKind.DHATU, upadesha = "भू")),
            context = DerivationalContext(rupa = Rupa(lakara = Lakara.LING))
        )
        assertTrue(HetuhetumatorLingSutra.matches(state))
    }

    @Test
    fun `derives tumun affix via KalaSamayaVelashuTumunSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "पठ्", TermKind.DHATU, upadesha = "पठ्")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.BHAVISYAT)
        )
        assertTrue(KalaSamayaVelashuTumunSutra.matches(state))
        assertEquals("तुमुन्", KalaSamayaVelashuTumunSutra.apply(state).state.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `derives ktvā and lyap affixes via SamanakartrkayohPurvakaleSutra`() {
        val state1 = DerivationState(
            terms = listOf(DerivationTerm("root", "भू", TermKind.DHATU, upadesha = "भू")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.BHAVA)
        )
        assertTrue(SamanakartrkayohPurvakaleSutra.matches(state1))
        assertEquals("क्त्वा", SamanakartrkayohPurvakaleSutra.apply(state1).state.allEffectiveTerms.last().upadesha)

        val state2 = DerivationState(
            terms = listOf(
                DerivationTerm("upasarga", "अनु", TermKind.PRATIPADIKA, upadesha = "अनु"),
                DerivationTerm("root", "भू", TermKind.DHATU, upadesha = "भू")
            ),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.BHAVA)
        )
        assertTrue(SamanakartrkayohPurvakaleSutra.matches(state2))
        assertEquals("ल्पँ", SamanakartrkayohPurvakaleSutra.apply(state2).state.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `derives tumun affix via ShakaDhrshJnAGlaGhatRabhabhLabhaprakramitumunSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "शक्", TermKind.DHATU, upadesha = "शक्")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.BHAVISYAT)
        )
        assertTrue(ShakaDhrshJnAGlaGhatRabhabhLabhaprakramitumunSutra.matches(state))
        assertEquals("तुमुन्", ShakaDhrshJnAGlaGhatRabhabhLabhaprakramitumunSutra.apply(state).state.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `establishes kartari sense via KartariKrtSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "कृ", TermKind.DHATU, upadesha = "कृ")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.KARTR_VEDANA)
        )
        assertTrue(KartariKrtSutra.matches(state))
    }
}
