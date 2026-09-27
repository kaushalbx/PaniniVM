package dev.panini.derivation

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PhonologicalIdentityTest {
    @Test
    fun `current identity is phonological and independent of upadesha spelling`() {
        val term = DerivationTerm("root", "लभ्", TermKind.DHATU, upadesha = "डुलभँष्")

        assertTrue(term.hasCurrentForm(PhonologicalIdentity.LABH))
        assertTrue(term.containsCurrentSequence(PhonologicalIdentity.LABH))
        assertFalse(term.hasCurrentForm(PhonologicalIdentity.SRAMBH))
    }
}
