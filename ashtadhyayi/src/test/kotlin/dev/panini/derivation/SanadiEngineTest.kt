package dev.panini.derivation

import dev.panini.shiksha.Samjna
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SanadiEngineTest {

    private val engine = SanadiEngine()

    @Test
    fun `causative nic derives bhavayati karayati and pathayati`() {
        val res1 = engine.derive(SanadiDerivationRequest("भू", Samjna.NIC))
        assertEquals("भावयति", res1.final.surface)
        assertTrue(res1.applications.any { it.sutra == "3.1.26" })
        assertTrue(res1.applications.any { it.sutra == "3.1.32" })

        val res2 = engine.derive(SanadiDerivationRequest("कृ", Samjna.NIC))
        assertEquals("कारयति", res2.final.surface)

        val res3 = engine.derive(SanadiDerivationRequest("पठ्", Samjna.NIC))
        assertEquals("पाठयति", res3.final.surface)
    }

    @Test
    fun `desiderative san derives bubhusati and pipathisati`() {
        val res1 = engine.derive(SanadiDerivationRequest("भू", Samjna.SAN))
        assertEquals("बुभूषति", res1.final.surface)
        assertTrue(res1.applications.any { it.sutra == "3.1.7" })
        assertTrue(res1.applications.any { it.sutra == "6.1.9" })

        val res2 = engine.derive(SanadiDerivationRequest("पठ्", Samjna.SAN))
        assertEquals("पिपाठिषति", res2.final.surface)
        assertTrue(res2.applications.any { it.sutra == "7.4.79" })
    }

    @Test
    fun `frequentative yan derives bobhuyate`() {
        val res1 = engine.derive(SanadiDerivationRequest("भू", Samjna.YAN))
        assertEquals("बोभूयते", res1.final.surface)
        assertTrue(res1.applications.any { it.sutra == "3.1.22" })
        assertTrue(res1.applications.any { it.sutra == "6.1.9" })
    }

    @Test
    fun `denominative kyac derives putriyati`() {
        val res1 = engine.derive(SanadiDerivationRequest("पुत्र", Samjna.KYAC))
        assertEquals("पुत्रीयति", res1.final.surface)
        assertTrue(res1.applications.any { it.sutra == "3.1.8" })
        assertTrue(res1.applications.any { it.sutra == "3.1.32" })
    }
}
