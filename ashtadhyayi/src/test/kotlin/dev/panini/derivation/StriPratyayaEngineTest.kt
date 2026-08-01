package dev.panini.derivation

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.shiksha.Samjna
import dev.panini.sutra.SutraStage
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
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
        assertTrue(res.applications.any { it.sutra == "4.1.73" })
    }

    @Test
    fun `ti derives yuvati`() {
        val res = engine.derive(StriPratyayaRequest("युवन्", Samjna.TI_PRATYAYA))
        assertEquals("युवति", res.final.surface)
        assertTrue(res.applications.any { it.sutra == "4.1.74" })
    }

    @Test
    fun `feminine provenance contains only explicitly staged rules`() {
        val requests = listOf(
            StriPratyayaRequest("अज", Samjna.TAP),
            StriPratyayaRequest("कर्तृ", Samjna.NIP),
            StriPratyayaRequest("गौर", Samjna.NIS),
            StriPratyayaRequest("नृ", Samjna.NIN),
            StriPratyayaRequest("युवन्", Samjna.TI_PRATYAYA),
        )

        requests.forEach { request ->
            val result = engine.derive(request)
            assertTrue(result.applications.isNotEmpty())
            result.applications.forEach { application ->
                val sutra = Ashtadhyayi.registry.require(application.sutra)
                assertTrue(sutra.stage != SutraStage.UNSPECIFIED, "${application.sutra} lacks pipeline metadata")
            }
            assertEquals(
                result.final,
                assertIs<DerivationEvent.Completed>(result.events.last()).finalState,
            )
        }
    }
}
