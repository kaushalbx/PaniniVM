package dev.sanskrit.ashtadhyayi.adhyaya7.pada4

import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationTerm
import dev.sanskrit.derivation.SamjnaAssignment
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.shiksha.Samjna
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class KuhohCuhSutraTest {
    @Test
    fun `7 4 62 changes initial ka-varga consonant of an abhyasa to ca-varga`() {
        val state = DerivationState(
            listOf(DerivationTerm("abhyasa", "कृ", TermKind.DHATU, upadesha = "कृ")),
            samjnas = setOf(SamjnaAssignment("abhyasa", Samjna.ABHYASA)),
        )

        assertTrue(KuhohCuhSutra.matches(state))
        val result = KuhohCuhSutra.apply(state).state
        assertEquals("चृ", result.terms.single().surface)
    }
}
