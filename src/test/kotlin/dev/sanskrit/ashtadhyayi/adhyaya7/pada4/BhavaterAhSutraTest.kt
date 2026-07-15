package dev.sanskrit.ashtadhyayi.adhyaya7.pada4

import dev.sanskrit.derivation.DerivationalContext
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationTerm
import dev.sanskrit.derivation.Lakara
import dev.sanskrit.derivation.Rupa
import dev.sanskrit.derivation.SamjnaAssignment
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.shiksha.Samjna
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BhavaterAhSutraTest {
    @Test
    fun `7 4 73 replaces final u of bhu abhyasa with inherent a in lit`() {
        val state = DerivationState(
            listOf(
                DerivationTerm("abhyasa", "भु", TermKind.DHATU, upadesha = "भू"),
                DerivationTerm("dhatu", "भू", TermKind.DHATU, upadesha = "भू"),
            ),
            samjnas = setOf(SamjnaAssignment("abhyasa", Samjna.ABHYASA)),
            context = DerivationalContext(rupa = Rupa(lakara = Lakara.LIT)),
        )

        assertTrue(BhavaterAhSutra.matches(state))
        val result = BhavaterAhSutra.apply(state).state
        assertEquals(listOf("भ", "भू"), result.terms.map { it.surface })
    }
}
