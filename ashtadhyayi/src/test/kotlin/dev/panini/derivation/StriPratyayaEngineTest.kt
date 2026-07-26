package dev.panini.derivation

import dev.panini.shiksha.Samjna
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StriPratyayaEngineTest {

    private val engine = StriPratyayaEngine()

    @Test
    fun `tap derives aja and bala`() {
        val res1 = engine.derive(StriPratyayaRequest("अज", Samjna.TAP))
        assertEquals("अजा", res1.final.surface)
        assertTrue(res1.applications.any { it.sutra == "4.1.4" })

        val res2 = engine.derive(StriPratyayaRequest("बाल", Samjna.TAP))
        assertEquals("बाला", res2.final.surface)
    }

    @Test
    fun `nip derives kartri and dandiNi`() {
        val res1 = engine.derive(StriPratyayaRequest("कर्तृ", Samjna.NIP))
        assertEquals("कर्त्री", res1.final.surface)
        assertTrue(res1.applications.any { it.sutra == "4.1.5" })

        val res2 = engine.derive(StriPratyayaRequest("दण्डिन्", Samjna.NIP))
        assertEquals("दण्डिनी", res2.final.surface)
    }

    @Test
    fun `nis derives gauri`() {
        val res = engine.derive(StriPratyayaRequest("गौर", Samjna.NIS))
        assertEquals("गौरी", res.final.surface)
        assertTrue(res.applications.any { it.sutra == "4.1.41" })
    }

    @Test
    fun `nin derives nari`() {
        val res = engine.derive(StriPratyayaRequest("नृ", Samjna.NIN))
        assertEquals("नारी", res.final.surface)
        assertTrue(res.applications.any { it.sutra == "4.1.65" })
    }

    @Test
    fun `ti derives yuvati`() {
        val res = engine.derive(StriPratyayaRequest("युवन्", Samjna.TI_PRATYAYA))
        assertEquals("युवति", res.final.surface)
        assertTrue(res.applications.any { it.sutra == "4.1.74" })
    }
}
