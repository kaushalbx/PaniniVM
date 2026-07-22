package dev.panini.ashtadhyayi.adhyaya7.pada4

import dev.panini.derivation.DerivationalContext
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.core.Lakara
import dev.panini.derivation.Rupa
import dev.panini.derivation.SamjnaAssignment
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
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
