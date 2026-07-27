package dev.panini.unadipatha

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.adadi.VaaDhatu
import dev.panini.dhatupatha.bhvadi.PumsDhatu
import dev.panini.dhatupatha.tanadi.KruDhatu
import dev.panini.unadipatha.model.Artha
import dev.panini.unadipatha.model.Samjna
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UnadiPathaTest {

    @Test
    fun testForwardPratyayaLookup() {
        val matches = UnadiPatha.findPratyaya(KruDhatu())
        assertTrue(matches.isNotEmpty(), "KruDhatu should match Uṇādi sūtras")

        val match1 = matches.firstOrNull { it.sutraNumber == "1.1" }
        assertTrue(match1 != null, "Should find sūtra 1.1 for KruDhatu")
        assertEquals("उण्", match1.pratyaya)
        assertEquals("उ", match1.pratyayaSurface)
        assertTrue(match1.itMarkers.contains(ItMarker.NIT))
    }

    @Test
    fun testReverseSamjnaLookupForKanin() {
        val matches = UnadiPatha.findSamjna(KruDhatu(), "कनिन्")
        assertTrue(matches.isNotEmpty(), "Should find reverse match for (KruDhatu, कनिन्)")

        val match = matches.first()
        assertEquals("4.1", match.sutraNumber)
        assertTrue(match.samjnas.contains(Samjna.Rudhi("कर्ण")))
        assertTrue(match.itMarkers.contains(ItMarker.KIT))
    }

    @Test
    fun testReverseSamjnaLookupForUn() {
        val matches = UnadiPatha.findSamjna(VaaDhatu(), "उण्")
        assertTrue(matches.isNotEmpty(), "Should find reverse match for (VaaDhatu, उण्)")

        val match = matches.first()
        assertEquals("1.1", match.sutraNumber)
        assertTrue(match.samjnas.contains(Samjna.Rudhi("वायु")))
        assertEquals(Artha.Karaka.KARTA, match.meaning)
    }

    @Test
    fun testEtymologicalNiruktaLookup() {
        val matches = UnadiPatha.findByWord("पुमः")
        assertTrue(matches.isNotEmpty(), "Should find etymological sūtra for 'पुमः'")

        val match = matches.first()
        assertEquals("3.1", match.sutraNumber)
        assertEquals("असुन्", match.pratyaya)
        assertEquals(PumsDhatu().upadesha, match.dhatu.upadesha)
    }
}
