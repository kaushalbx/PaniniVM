package dev.panini.derivation

import dev.panini.core.SupAffix
import kotlin.test.*

class SarvanamasthanaAffixIdentityTest {
    @Test fun `five sup identities do not depend on generated ids`() {
        val eligible = setOf(SupAffix.SU, SupAffix.AU, SupAffix.JAS, SupAffix.AM, SupAffix.AUT)
        for (identity in SupAffix.entries) {
            val term = identity.term().copy(id = "arbitrary")
            assertEquals(identity in eligible, term.hasSarvanamasthanaSupIdentity(), identity.name)
            assertFalse(term.copy(kind = TermKind.PRATIPADIKA).hasSarvanamasthanaSupIdentity())
            assertEquals(identity in eligible, term.copy(upadesha = "replacement", surface = "इ").hasSarvanamasthanaSupIdentity())
        }
        assertFalse(DerivationTerm("sup-su", "इ", TermKind.PRATYAYA).hasSarvanamasthanaSupIdentity())
    }
}
