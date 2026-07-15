package dev.sanskrit.ashtadhyayi.adhyaya8.pada4

import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationTerm
import dev.sanskrit.derivation.SamjnaAssignment
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.shiksha.Samjna
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AbhyaseCarCaSutraTest {
    @Test
    fun `8 4 54 changes bha of an abhyasa to ba`() {
        val state = DerivationState(
            listOf(
                DerivationTerm("abhyasa", "भ", TermKind.DHATU, upadesha = "भू"),
                DerivationTerm("dhatu", "भू", TermKind.DHATU, upadesha = "भू"),
            ),
            samjnas = setOf(SamjnaAssignment("abhyasa", Samjna.ABHYASA)),
        )

        assertTrue(AbhyaseCarCaSutra.matches(state))
        val result = AbhyaseCarCaSutra.apply(state).state
        assertEquals(listOf("ब", "भू"), result.terms.map { it.surface })
    }
}
