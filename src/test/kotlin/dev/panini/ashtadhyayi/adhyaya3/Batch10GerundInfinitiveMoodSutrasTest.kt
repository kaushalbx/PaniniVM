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
import dev.panini.derivation.DerivationalContext
import dev.panini.derivation.DerivationalMeaning
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Batch10GerundInfinitiveMoodSutrasTest {

    @Test
    fun `test 3 3 18 BhaveSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "पच्", TermKind.DHATU, upadesha = "पच्")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.BHAVA)
        )
        assertTrue(BhaveSutra.matches(state))
        assertEquals("घञ्", BhaveSutra.apply(state).state.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `test 3 3 56 EchaIgGhanSutra`() {
        val state = DerivationState(terms = listOf(DerivationTerm("ghanj", "अ", TermKind.PRATYAYA, upadesha = "घञ्")))
        assertTrue(EchaIgGhanSutra.matches(state))
    }

    @Test
    fun `test 3 3 57 RadorApsutra`() {
        assertTrue(RadorApsutra.matches("कृ"))
        assertEquals("अप्", RadorApsutra.apply("कृ"))
    }

    @Test
    fun `test 3 3 94 StriyamKtinSutra`() {
        assertTrue(StriyamKtinSutra.matches("कृ"))
        assertEquals("क्तिन्", StriyamKtinSutra.apply("कृ"))
    }

    @Test
    fun `test 3 3 114 NapumsakeBhaveKtahSutra`() {
        assertTrue(NapumsakeBhaveKtahSutra.matches("हस्"))
        assertEquals("क्त", NapumsakeBhaveKtahSutra.apply("हस्"))
    }

    @Test
    fun `test 3 3 156 HetuhetumatorLingSutra`() {
        assertTrue(HetuhetumatorLingSutra.matches("हेतु"))
        assertEquals("लिङ्", HetuhetumatorLingSutra.apply("हेतु"))
    }

    @Test
    fun `test 3 3 167 KalaSamayaVelashuTumunSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "पठ्", TermKind.DHATU, upadesha = "पठ्")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.BHAVISYAT)
        )
        assertTrue(KalaSamayaVelashuTumunSutra.matches(state))
        assertEquals("तुमुन्", KalaSamayaVelashuTumunSutra.apply(state).state.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `test 3 4 21 SamanakartrkayohPurvakaleSutra`() {
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
        assertEquals("ल्पँ", SamanakartrkayohPurvakaleSutra.apply(state2).state.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `test 3 4 65 ShakaDhrshJnAGlaGhatRabhabhLabhaprakramitumunSutra`() {
        assertTrue(ShakaDhrshJnAGlaGhatRabhabhLabhaprakramitumunSutra.matches("शक्"))
        assertEquals("तुमुन्", ShakaDhrshJnAGlaGhatRabhabhLabhaprakramitumunSutra.apply("शक्"))
    }

    @Test
    fun `test 3 4 67 KartariKrtSutra`() {
        assertTrue(KartariKrtSutra.matches("कर्तृ"))
        assertEquals("कर्तृ", KartariKrtSutra.apply("कर्तृ"))
    }
}
