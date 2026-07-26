package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya1.pada4.GhiSutra
import dev.panini.shiksha.Samjna
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GhiSutraTest {
    @Test
    fun `assigns ghi to a short i stem`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("stem", "कवि", TermKind.PRATIPADIKA)),
        )

        assertTrue(GhiSutra.matches(state))
        assertTrue(GhiSutra.apply(state).state.samjnas.any { it.targetId == "stem" && it.samjna == Samjna.GHI })
    }

    @Test
    fun `does not assign ghi to a nadi-designated stem`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("stem", "नदी", TermKind.PRATIPADIKA)),
            samjnas = setOf(SamjnaAssignment("stem", Samjna.NADI)),
        )

        assertFalse(GhiSutra.matches(state))
    }

    @Test
    fun `excludes sakhi`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("stem", "सखि", TermKind.PRATIPADIKA)),
        )

        assertFalse(GhiSutra.matches(state))
    }
}
