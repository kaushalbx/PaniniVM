package dev.panini.ashtadhyayi.adhyaya7.pada4

import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.SamjnaAssignment
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
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
