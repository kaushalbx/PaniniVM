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

class AtaAdesSutraTest {
    @Test
    fun `7 4 70 lengthens initial a of a lit abhyasa`() {
        val state = DerivationState(
            listOf(DerivationTerm("abhyasa", "अट्", TermKind.DHATU, upadesha = "अट्")),
            samjnas = setOf(SamjnaAssignment("abhyasa", Samjna.ABHYASA)),
            context = DerivationalContext(rupa = Rupa(lakara = Lakara.LIT)),
        )

        assertTrue(AtaAdesSutra.matches(state))
        val result = AtaAdesSutra.apply(state).state
        assertEquals("आट्", result.terms.single().surface)
    }
}
