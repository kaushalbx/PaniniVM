package dev.panini.derivation

import dev.panini.shiksha.Samjna
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ParticipleEngineTest {

    private val engine = ParticipleEngine()

    @Test
    fun `satr derives bhavat`() {
        val res = engine.derive(ParticipleDerivationRequest("भू", Samjna.SATR))
        assertEquals("भवत्", res.final.surface)
        assertTrue(res.applications.any { it.sutra == "3.2.124" })
    }

    @Test
    fun `sanac derives labhamana`() {
        val res = engine.derive(ParticipleDerivationRequest("लभ्", Samjna.SANAC))
        assertEquals("लभमान", res.final.surface)
        assertTrue(res.applications.any { it.sutra == "3.2.124" })
    }

    @Test
    fun `kvasu derives babhuvas`() {
        val res = engine.derive(ParticipleDerivationRequest("भू", Samjna.KVASU))
        assertEquals("बभूवस्", res.final.surface)
        assertTrue(res.applications.any { it.sutra == "3.2.106" })
    }

    @Test
    fun `kanac derives lebhana`() {
        val res = engine.derive(ParticipleDerivationRequest("लभ्", Samjna.KANAC))
        assertEquals("लेभान", res.final.surface)
        assertTrue(res.applications.any { it.sutra == "3.2.107" })
    }

    @Test
    fun `unadi derives vayu and karu`() {
        val res1 = engine.derive(ParticipleDerivationRequest("वा", Samjna.UNADI))
        assertEquals("वायु", res1.final.surface)
        assertTrue(res1.applications.any { it.sutra == "3.3.1" })

        val res2 = engine.derive(ParticipleDerivationRequest("कृ", Samjna.UNADI))
        assertEquals("कारु", res2.final.surface)
        assertTrue(res2.applications.any { it.sutra == "3.3.1" })
    }
}
