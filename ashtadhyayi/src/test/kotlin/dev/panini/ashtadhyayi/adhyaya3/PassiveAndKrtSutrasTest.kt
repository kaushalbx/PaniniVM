package dev.panini.ashtadhyayi.adhyaya3

import dev.panini.ashtadhyayi.adhyaya3.pada1.SarvadhatukeYakSutra
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


}
