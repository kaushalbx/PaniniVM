package dev.panini.derivation

import dev.panini.core.SupAffix
import dev.panini.core.KrtAffix
import dev.panini.core.SanadiAffix
import dev.panini.core.TingAffix
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AffixExtensionsTest {
    @Test
    fun `typed sup matching recognizes direct and retained sthani identity`() {
        assertTrue(SupAffix.JAS.term().matchesSupAffix(SupAffix.JAS))
        assertTrue(
            DerivationTerm(
                id = "sup-jas",
                surface = "शि",
                kind = TermKind.PRATYAYA,
                upadesha = "शि",
                sthaniProps = SthaniProperties(upadesha = SupAffix.JAS.upadesha, itMarkers = emptySet()),
            ).matchesSupAffix(SupAffix.JAS),
        )
    }

    @Test
    fun `typed sanadi and krt identities support construction matching and input aliases`() {
        assertTrue(SanadiAffix.NIC.term("nic").matchesAffix(SanadiAffix.NIC))
        assertTrue(KrtAffix.KTAVATU.term("ktavatu").matchesAffix(KrtAffix.KTAVATU))
        assertEquals(KrtAffix.ANIYAR, KrtAffix.fromUpadesha("अनीयर"))
        assertTrue(DerivationTerm("aniyar", "अनीयर", TermKind.PRATYAYA).matchesAffix(KrtAffix.ANIYAR))
        assertEquals(KrtAffix.YAT, KrtAffix.fromUpadesha("यत"))
        assertEquals(KrtAffix.LYUT, KrtAffix.fromUpadesha("अन"))
    }

    @Test
    fun `sup and ting inventories participate in the common typed affix API`() {
        assertTrue(SupAffix.JAS.term().matchesAffix(SupAffix.JAS))
        assertTrue(TingAffix.SIP.term().matchesAffix(TingAffix.SIP))
        assertTrue(TingAffix.ATAM.term().matchesAnyAffix(TingAffix.ATAM, TingAffix.ATHAM))
    }
}
