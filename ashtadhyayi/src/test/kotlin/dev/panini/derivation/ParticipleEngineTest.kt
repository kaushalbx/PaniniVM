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
        assertTrue(setOf("3.2.124", "3.1.68", "7.3.84", "1.3.9").all { sutra ->
            res.applications.any { it.sutra == sutra }
        })
        res.final.requireCompleteItProcessing()
    }

    @Test
    fun `sanac derives labhamana`() {
        val res = engine.derive(ParticipleDerivationRequest("लभ्", Samjna.SANAC))
        assertEquals("लभमान", res.final.surface)
        assertTrue(setOf("3.2.124", "3.1.68", "7.2.82", "1.1.46", "1.3.9").all { sutra ->
            res.applications.any { it.sutra == sutra }
        })
        res.final.requireCompleteItProcessing()
    }

    @Test
    fun `kvasu derives babhuvas`() {
        val res = engine.derive(ParticipleDerivationRequest("भू", Samjna.KVASU))
        assertEquals("बभूवस्", res.final.surface)
        assertTrue(setOf("3.2.106", "6.1.8", "7.4.73", "8.4.54", "1.3.9").all { sutra -> res.applications.any { it.sutra == sutra } })
        res.final.requireCompleteItProcessing()
    }

    @Test
    fun `kanac derives lebhana`() {
        val res = engine.derive(ParticipleDerivationRequest("लभ्", Samjna.KANAC))
        assertEquals("लेभान", res.final.surface)
        assertTrue(setOf("3.2.107", "6.1.8", "6.4.120", "1.3.9").all { sutra -> res.applications.any { it.sutra == sutra } })
        res.final.requireCompleteItProcessing()
    }

    @Test
    fun `unadi derives vayu and karu`() {
        val res1 = engine.derive(ParticipleDerivationRequest("वा", Samjna.UNADI))
        assertEquals("वायु", res1.final.surface)
        assertTrue(res1.applications.any { it.sutra == "3.3.1" })
        assertTrue(res1.applications.any { it.sutra == "7.3.33" })
        res1.final.requireCompleteItProcessing()

        val res2 = engine.derive(ParticipleDerivationRequest("कृ", Samjna.UNADI))
        assertEquals("कारु", res2.final.surface)
        assertTrue(res2.applications.any { it.sutra == "7.2.115" })
        res2.final.requireCompleteItProcessing()
        assertTrue(res2.applications.any { it.sutra == "3.3.1" })
    }

    @Test
    fun `unadi derives manas and caksus stems for asun and usi`() {
        val res1 = engine.derive(ParticipleDerivationRequest("मन्", Samjna.ASUN))
        assertEquals("मनस्", res1.final.surface)
        res1.final.requireCompleteItProcessing()

        val res2 = engine.derive(ParticipleDerivationRequest("चक्ष्", Samjna.USI))
        assertEquals("चक्षुस्", res2.final.surface)
        res2.final.requireCompleteItProcessing()
    }
}
