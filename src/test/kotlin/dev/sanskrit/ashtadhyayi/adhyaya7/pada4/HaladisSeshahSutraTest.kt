package dev.sanskrit.ashtadhyayi.adhyaya7.pada4

import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationTerm
import dev.sanskrit.derivation.SamjnaAssignment
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.shiksha.Samjna
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
}
