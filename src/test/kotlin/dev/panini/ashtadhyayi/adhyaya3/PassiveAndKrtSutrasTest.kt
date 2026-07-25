package dev.panini.ashtadhyayi.adhyaya3

import dev.panini.ashtadhyayi.adhyaya3.pada1.KrdAticSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.SarvadhatukeYakSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.TavyattavyanIyarahSutra
import dev.panini.core.Prayoga
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

class PassiveAndKrtSutrasTest {

    @Test
    fun `derives yak vikaraṇa in karmaṇi via SarvadhatukeYakSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "कृ", TermKind.DHATU, upadesha = "कृ")),
            context = DerivationalContext(rupa = Rupa(prayoga = Prayoga.KARMANI))
        )
        val result = DerivationEngine(listOf(SarvadhatukeYakSutra)).derive(state)
        assertTrue(result.applications.any { it.sutra == "3.1.67" })
        assertEquals("यक्", result.final.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `assigns kṛt saṃjñā via KrdAticSutra`() {
        val state = DerivationState(
            terms = listOf(
                DerivationTerm("root", "कृ", TermKind.DHATU, upadesha = "कृ"),
                DerivationTerm("ghanj", "अ", TermKind.PRATYAYA, upadesha = "घञ्")
            )
        )
        val result = DerivationEngine(listOf(KrdAticSutra)).derive(state)
        assertTrue(result.applications.any { it.sutra == "3.1.93" })
    }

    @Test
    fun `derives tavyat affix via TavyattavyanIyarahSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "कृ", TermKind.DHATU, upadesha = "कृ")),
            context = DerivationalContext(requestedMeaning = DerivationalMeaning.BHAVA)
        )
        val result = DerivationEngine(listOf(TavyattavyanIyarahSutra)).derive(state)
        assertTrue(result.applications.any { it.sutra == "3.1.96" })
        assertEquals("तव्यत्", result.final.allEffectiveTerms.last().upadesha)
    }
}
