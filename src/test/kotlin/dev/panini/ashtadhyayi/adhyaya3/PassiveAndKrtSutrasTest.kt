package dev.panini.ashtadhyayi.adhyaya3

import dev.panini.ashtadhyayi.adhyaya3.pada1.KrdAticSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.SarvadhatukeYakSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.TavyattavyanIyarahSutra
import dev.panini.core.Prayoga
import dev.panini.derivation.DerivationalContext
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.Rupa
import dev.panini.derivation.TermKind
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PassiveAndKrtSutrasTest {

    @Test
    fun `test 3 1 67 SarvadhatukeYakSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "कृ", TermKind.DHATU, upadesha = "कृ")),
            context = DerivationalContext(rupa = Rupa(prayoga = Prayoga.KARMANI))
        )
        assertTrue(SarvadhatukeYakSutra.matches(state))
        assertEquals("यक्", SarvadhatukeYakSutra.apply(state).state.allEffectiveTerms.last().upadesha)
    }

    @Test
    fun `test 3 1 93 KrdAticSutra`() {
        assertTrue(KrdAticSutra.matches("घञ्"))
        assertEquals("घञ्", KrdAticSutra.apply("घञ्"))
    }

    @Test
    fun `test 3 1 96 TavyattavyanIyarahSutra`() {
        assertTrue(TavyattavyanIyarahSutra.matches("कृ"))
        assertEquals("तव्यत्", TavyattavyanIyarahSutra.apply("कृ"))
    }
}
