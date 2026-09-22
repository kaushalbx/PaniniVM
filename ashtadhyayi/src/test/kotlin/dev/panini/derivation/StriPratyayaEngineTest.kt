package dev.panini.derivation

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.shiksha.Samjna
import dev.panini.ashtadhyayi.adhyaya4.pada1.AjadyatasTapSutra
import dev.panini.ashtadhyayi.adhyaya4.pada1.StriyamSutra
import dev.panini.core.Linga
import dev.panini.sutra.SutraStage
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class StriPratyayaEngineTest {

    @Test
    fun `feminine adhikara and tap cannot enter a masculine derivation`() {
        val masculine = SubantaDerivationRequest(
            "वासुदेव",
            dev.panini.core.Vibhakti.PRATHAMA,
            dev.panini.core.Vacana.EKAVACANA,
            Linga.PUMS,
        ).initialState()
        kotlin.test.assertFalse(StriyamSutra.matches(masculine))
        kotlin.test.assertFalse(AjadyatasTapSutra.matches(masculine.copy(activeAdhikaras = setOf("4.1.3"))))
    }

    private val engine = StriPratyayaEngine()

    @Test
    fun `tap derives aja and bala`() {
        val res1 = engine.derive(StriPratyayaRequest("अज", Samjna.TAP))
        assertEquals("अजा", res1.final.surface)
        assertTrue(res1.applications.any { it.sutra == "4.1.4" })
        assertTrue(res1.applications.any { it.sutra == "3.1.4" })
        assertEquals("अजा", res1.svaraResult?.word)

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
