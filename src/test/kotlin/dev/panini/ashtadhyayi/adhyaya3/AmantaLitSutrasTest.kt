package dev.panini.ashtadhyayi.adhyaya3

import dev.panini.ashtadhyayi.adhyaya3.pada1.IjashChaSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.KasPratyayadAmAmantreLitSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.KrnChanuprayujyateLitSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.UshavidajabhyashChaSutra
import dev.panini.core.Lakara
import dev.panini.derivation.DerivationalContext
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.Rupa
import dev.panini.derivation.TermKind
import kotlin.test.Test
import kotlin.test.assertTrue

class AmantaLitSutrasTest {

    @Test
    fun `test 3 1 35 KasPratyayadAmAmantreLitSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "कास्", TermKind.DHATU, upadesha = "कास्")),
            context = DerivationalContext(rupa = Rupa(lakara = Lakara.LIT))
        )
        assertTrue(KasPratyayadAmAmantreLitSutra.matches(state))
        assertTrue(KasPratyayadAmAmantreLitSutra.apply(state).state.allEffectiveTerms.any { it.upadesha == "आम्" })
    }

    @Test
    fun `test 3 1 36 IjashChaSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "ईक्ष्", TermKind.DHATU, upadesha = "ईक्ष्")),
            context = DerivationalContext(rupa = Rupa(lakara = Lakara.LIT))
        )
        assertTrue(IjashChaSutra.matches(state))
        assertTrue(IjashChaSutra.apply(state).state.allEffectiveTerms.any { it.upadesha == "आम्" })
    }

    @Test
    fun `test 3 1 38 UshavidajabhyashChaSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "विद्", TermKind.DHATU, upadesha = "विद्")),
            context = DerivationalContext(rupa = Rupa(lakara = Lakara.LIT))
        )
        assertTrue(UshavidajabhyashChaSutra.matches(state))
        assertTrue(UshavidajabhyashChaSutra.apply(state).state.allEffectiveTerms.any { it.upadesha == "आम्" })
    }

    @Test
    fun `test 3 1 40 KrnChanuprayujyateLitSutra`() {
        val state = DerivationState(
            terms = listOf(
                DerivationTerm("root", "ईक्ष्", TermKind.DHATU, upadesha = "ईक्ष्"),
                DerivationTerm("am", "आम्", TermKind.PRATYAYA, upadesha = "आम्")
            ),
            context = DerivationalContext(rupa = Rupa(lakara = Lakara.LIT))
        )
        assertTrue(KrnChanuprayujyateLitSutra.matches(state))
    }
}
