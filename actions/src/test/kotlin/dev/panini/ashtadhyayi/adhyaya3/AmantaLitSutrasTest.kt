package dev.panini.ashtadhyayi.adhyaya3

import dev.panini.ashtadhyayi.adhyaya3.pada1.IjashChaSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.KasPratyayadAmAmantreLitSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.KrnChanuprayujyateLitSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.UshavidajabhyashChaSutra
import dev.panini.core.Lakara
import dev.panini.derivation.DerivationEngine
import dev.panini.derivation.DerivationalContext
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.Rupa
import dev.panini.derivation.TermKind
import kotlin.test.Test
import kotlin.test.assertTrue

class AmantaLitSutrasTest {

    @Test
    fun `derives ām affix in Liṭ via KasPratyayadAmAmantreLitSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "कास्", TermKind.DHATU, upadesha = "कास्")),
            context = DerivationalContext(rupa = Rupa(lakara = Lakara.LIT))
        )
        val result = DerivationEngine(listOf(KasPratyayadAmAmantreLitSutra)).derive(state)
        assertTrue(result.applications.any { it.sutra == "3.1.35" })
        assertTrue(result.final.allEffectiveTerms.any { it.upadesha == "आम्" })
    }

    @Test
    fun `derives ām affix in Liṭ for ijc roots via IjashChaSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "ईक्ष्", TermKind.DHATU, upadesha = "ईक्ष्")),
            context = DerivationalContext(rupa = Rupa(lakara = Lakara.LIT))
        )
        val result = DerivationEngine(listOf(IjashChaSutra)).derive(state)
        assertTrue(result.applications.any { it.sutra == "3.1.36" })
        assertTrue(result.final.allEffectiveTerms.any { it.upadesha == "आम्" })
    }

    @Test
    fun `derives ām affix in Liṭ for uṣ vid jā via UshavidajabhyashChaSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "विद्", TermKind.DHATU, upadesha = "विद्")),
            context = DerivationalContext(rupa = Rupa(lakara = Lakara.LIT))
        )
        val result = DerivationEngine(listOf(UshavidajabhyashChaSutra)).derive(state)
        assertTrue(result.applications.any { it.sutra == "3.1.38" })
        assertTrue(result.final.allEffectiveTerms.any { it.upadesha == "आम्" })
    }

    @Test
    fun `applies auxiliary after ām in Liṭ via KrnChanuprayujyateLitSutra`() {
        val state = DerivationState(
            terms = listOf(
                DerivationTerm("root", "ईक्ष्", TermKind.DHATU, upadesha = "ईक्ष्"),
                DerivationTerm("am", "आम्", TermKind.PRATYAYA, upadesha = "आम्")
            ),
            context = DerivationalContext(rupa = Rupa(lakara = Lakara.LIT))
        )
        val result = DerivationEngine(listOf(KrnChanuprayujyateLitSutra)).derive(state)
        assertTrue(result.applications.any { it.sutra == "3.1.40" })
    }
}
