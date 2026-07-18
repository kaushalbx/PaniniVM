package dev.panini.ashtadhyayi.adhyaya7.pada4

import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.SamjnaAssignment
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UratSutraTest {
    @Test
    fun `7 4 66 replaces vocalic r in an abhyasa with a`() {
        val state = DerivationState(
            listOf(DerivationTerm("abhyasa", "चृ", TermKind.DHATU, upadesha = "कृ")),
            samjnas = setOf(SamjnaAssignment("abhyasa", Samjna.ABHYASA)),
        )

        assertTrue(UratSutra.matches(state))
        val result = UratSutra.apply(state).state
        assertEquals("च", result.terms.single().surface)
    }
}
