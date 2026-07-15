package dev.sanskrit.ashtadhyayi.adhyaya6.pada4

import dev.sanskrit.derivation.DerivationalContext
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationTerm
import dev.sanskrit.derivation.Lakara
import dev.sanskrit.derivation.Rupa
import dev.sanskrit.derivation.TermKind
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BhuvoVuglunglitoSutraTest {
    @Test
    fun `6 4 88 inserts the effective vuk after bhu in lit`() {
        val state = DerivationState(
            listOf(DerivationTerm("dhatu", "भू", TermKind.DHATU, upadesha = "भू")),
            context = DerivationalContext(rupa = Rupa(lakara = Lakara.LIT)),
        )

        assertTrue(BhuvoVuglunglitoSutra.matches(state))
        val result = BhuvoVuglunglitoSutra.apply(state).state
        assertEquals(listOf("भू", "व्"), result.terms.map { it.surface })
        assertEquals("वुक्", result.terms[1].upadesha)
    }
}
