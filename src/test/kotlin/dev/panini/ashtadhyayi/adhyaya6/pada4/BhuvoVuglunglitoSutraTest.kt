package dev.panini.ashtadhyayi.adhyaya6.pada4

import dev.panini.derivation.DerivationalContext
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.core.Lakara
import dev.panini.derivation.Rupa
import dev.panini.derivation.TermKind
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BhuvoVuglunglitoSutraTest {
    @Test
    fun `6 4 88 inserts the effective vuk after bhu in lit`() {
        val state = DerivationState(
            listOf(
                DerivationTerm("dhatu", "भू", TermKind.DHATU, upadesha = "भू"),
                DerivationTerm("ending", "अ", TermKind.PRATYAYA, upadesha = "तिप्"),
            ),
            context = DerivationalContext(rupa = Rupa(lakara = Lakara.LIT)),
        )

        assertTrue(BhuvoVuglunglitoSutra.matches(state))
        val result = BhuvoVuglunglitoSutra.apply(state).state
        assertEquals(listOf("भू", "व्", "अ"), result.terms.map { it.surface })
        assertEquals("वुक्", result.terms[1].upadesha)
    }
}
