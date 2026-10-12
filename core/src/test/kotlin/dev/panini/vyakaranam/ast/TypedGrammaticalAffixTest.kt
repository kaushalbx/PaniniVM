package dev.panini.vyakaranam.ast

import dev.panini.core.SanadiAffix
import dev.panini.core.SupAffix
import dev.panini.core.TingAffix
import dev.panini.core.Vibhakti
import dev.panini.core.Vacana
import kotlin.test.*

class TypedGrammaticalAffixTest {
    @Test fun `slot compatibility compares case and number together for every pair of endings`() {
        val endings = SupAffix.entries.map { it.upadesha }.distinct() + "ङसिँ" + "unknown"
        for (left in endings) for (right in endings) {
            val expected = SupAffix.candidates(left).any { a -> SupAffix.candidates(right).any { b ->
                a.vibhakti == b.vibhakti && a.vacana == b.vacana
            } }
            assertEquals(expected, SupPratyaya(left, left).sharesSlotWith(SupPratyaya(right, right)), "$left / $right")
        }
        assertFalse(SupPratyaya("भ्याम्", "भ्याम्").sharesSlotWith(SupPratyaya("भ्यस्", "भ्यस्")))
        assertTrue(SupPratyaya("ङसि", "ङसि").sharesSlotWith(SupPratyaya("ङसिँ", "ङसिँ")))
    }

    @Test fun `context can constrain any syncretic slot without discarding alternatives`() {
        val dual = SupPratyaya("ignored", "भ्याम्")
        for (case in listOf(Vibhakti.TRTIYA, Vibhakti.CHATURTHI, Vibhakti.PANCHAMI)) {
            assertTrue(dual.supports(case, Vacana.DVIVACANA))
            assertFalse(dual.supports(case, Vacana.EKAVACANA))
        }
        val oblique = SupPratyaya("ignored", "ओस्")
        assertTrue(oblique.supports(Vibhakti.SASTHI))
        assertTrue(oblique.supports(Vibhakti.SAPTAMI))
        assertFalse(oblique.supports(Vibhakti.PRATHAMA))
        assertFalse(SupPratyaya("ignored", "unknown").supports(Vibhakti.PRATHAMA))
        assertEquals(3, dual.affixCandidates.size)
    }

    @Test fun `sup identity preserves syncretism and canonical spelling`() {
        assertEquals(3, SupPratyaya("ignored", "भ्याम्").affixCandidates.size)
        assertEquals(SupAffix.candidates("ङसि"), SupPratyaya("ignored", "ङसिँ").affixCandidates)
        assertEquals(listOf(SupAffix.AM), SupPratyaya("ignored", "अम्").affixCandidates)
        assertTrue(SupPratyaya("ignored", "unknown").affixCandidates.isEmpty())
    }

    @Test fun `verbal affix identities do not discard unknown input`() {
        assertEquals(TingAffix.TIP, TingPratyaya("ignored", "तिप्").affix)
        assertNull(TingPratyaya("ignored", "unknown").affix)
        val root = DhatuPrakriti("ignored", "जन्", listOf("णिच्", "unknown"))
        assertEquals(listOf(SanadiAffix.NIC, null), root.sanadiAffixes)
        assertNotEquals(listOf(SanadiAffix.NIC), root.sanadiAffixes)
    }
}
