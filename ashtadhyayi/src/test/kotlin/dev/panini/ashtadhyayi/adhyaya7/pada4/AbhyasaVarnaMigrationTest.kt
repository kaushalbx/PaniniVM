package dev.panini.ashtadhyayi.adhyaya7.pada4

import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AbhyasaVarnaMigrationTest {
    @Test
    fun `7 4 79 treats an inherent final a as a phonological vowel`() {
        val state = DerivationState(
            listOf(
                DerivationTerm("abhyasa", "प", TermKind.DHATU),
                DerivationTerm("san", "सन्", TermKind.PRATYAYA, upadesha = "सन्"),
            ),
        )

        assertTrue(SanyAtaSutra.matches(state))
        assertEquals("पि", SanyAtaSutra.apply(state).state.terms.first().surface)
    }

    @Test
    fun `7 4 82 applies guna to the final ik varna independent of matra spelling`() {
        val state = DerivationState(
            listOf(
                DerivationTerm("abhyasa", "कु", TermKind.DHATU),
                DerivationTerm("yang", "यङ्", TermKind.PRATYAYA, upadesha = "यङ्"),
            ),
        )

        assertTrue(GunoYangiSutra.matches(state))
        assertEquals("को", GunoYangiSutra.apply(state).state.terms.first().surface)
    }
}
