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
