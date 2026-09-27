package dev.panini.ashtadhyayi.adhyaya7.pada4

import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.SamjnaAssignment
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HaladisSeshahSutraTest {
    @Test
    fun `7 4 60 retains only the initial consonant of an abhyasa`() {
        val state = DerivationState(
            listOf(DerivationTerm("abhyasa", "सद्", TermKind.DHATU, upadesha = "सद्")),
            samjnas = setOf(SamjnaAssignment("abhyasa", Samjna.ABHYASA)),
        )

        assertTrue(HaladisSeshahSutra.matches(state))
        val result = HaladisSeshahSutra.apply(state).state
        assertEquals("स", result.terms.single().surface)
    }

    @Test
    fun `7 4 60 removes later consonants by varna position in a conjunct spelling`() {
        val state = DerivationState(
            listOf(DerivationTerm("abhyasa", "स्तद्", TermKind.DHATU, upadesha = "स्तद्")),
            samjnas = setOf(SamjnaAssignment("abhyasa", Samjna.ABHYASA)),
        )

        val result = HaladisSeshahSutra.apply(state).state

        assertEquals("स", result.terms.single().surface)
    }
}
