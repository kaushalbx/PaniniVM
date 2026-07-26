package dev.panini.ashtadhyayi.adhyaya7.pada4

import dev.panini.core.Lakara
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.DerivationalContext
import dev.panini.derivation.Rupa
import dev.panini.derivation.TermKind
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AshnotesCaSutraTest {
    @Test
    fun `7 4 72 inserts effective nut after a lengthened abhyasa before ash`() {
        val state = DerivationState(
            listOf(
                DerivationTerm("abhyasa", "आ", TermKind.DHATU, upadesha = "अ"),
                DerivationTerm("dhatu", "अश्", TermKind.DHATU, upadesha = "अश्"),
            ),
            context = DerivationalContext(rupa = Rupa(lakara = Lakara.LIT)),
        )

        assertTrue(AshnotesCaSutra.matches(state))
        val result = AshnotesCaSutra.apply(state).state
        assertEquals(listOf("आ", "न्", "अश्"), result.terms.map { it.surface })
        assertEquals("नुट्", result.terms[1].upadesha)
    }
}
