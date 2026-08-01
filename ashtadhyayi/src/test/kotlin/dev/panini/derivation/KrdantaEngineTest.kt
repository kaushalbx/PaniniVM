package dev.panini.derivation

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.shiksha.Samjna
import dev.panini.sutra.SutraStage
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class KrdantaEngineTest {

    private val engine = KrdantaEngine()

    @Test
    fun `ktva generates bhutva and krtva`() {
        val res1 = engine.derive(KrdantaDerivationRequest("भू", Samjna.KTVA))
        assertEquals("भूत्वा", res1.final.surface)
        assertTrue(res1.applications.any { it.sutra == "3.4.21" })

        val res2 = engine.derive(KrdantaDerivationRequest("कृ", Samjna.KTVA))
        assertEquals("कृत्वा", res2.final.surface)
    }

    @Test
    fun `lyap generates sambhuya and anukrtya with tuk-agama`() {
        val res1 = engine.derive(KrdantaDerivationRequest("भू", Samjna.KTVA, upasarga = "सम्"))
        assertEquals("संभूय", res1.final.surface)
        assertTrue(res1.applications.any { it.sutra == "3.4.21" || it.sutra == "7.1.37" })

        val res2 = engine.derive(KrdantaDerivationRequest("कृ", Samjna.KTVA, upasarga = "अनु"))
        assertEquals("अनुकृत्य", res2.final.surface)
        assertTrue(res2.applications.any { it.sutra == "3.4.21" || it.sutra == "7.1.37" })
        assertTrue(res2.applications.any { it.sutra == "6.1.71" })
    }

    @Test
    fun `tumun generates bhavitum and kartum`() {
        val res1 = engine.derive(KrdantaDerivationRequest("भू", Samjna.TUMUN))
        assertEquals("भवितुम्", res1.final.surface)
        assertTrue(res1.applications.any { it.sutra == "3.3.158" })
        assertTrue(res1.applications.any { it.sutra == "7.2.35" })

        val res2 = engine.derive(KrdantaDerivationRequest("कृ", Samjna.TUMUN))
        assertEquals("कर्तुम्", res2.final.surface)
    }

    @Test
    fun `krtya affixes generate kartavya karaniya and karya`() {
        val res1 = engine.derive(KrdantaDerivationRequest("कृ", Samjna.TAVYA))
        assertEquals("कर्तव्य", res1.final.surface)
        assertTrue(res1.applications.any { it.sutra == "3.1.96" })

        val res2 = engine.derive(KrdantaDerivationRequest("कृ", Samjna.ANIYAR))
        assertEquals("करणीय", res2.final.surface)
        assertTrue(res2.applications.any { it.sutra == "3.1.96" })

        val res3 = engine.derive(KrdantaDerivationRequest("कृ", Samjna.NYAT))
        assertEquals("कार्य", res3.final.surface)
        assertTrue(res3.applications.any { it.sutra == "3.1.124" })
    }

    @Test
    fun `nistha affixes generate bhuta and krta`() {
        val res1 = engine.derive(KrdantaDerivationRequest("भू", Samjna.KTA))
        assertEquals("भूत", res1.final.surface)
        assertTrue(res1.applications.any { it.sutra == "1.1.26" })

        val res2 = engine.derive(KrdantaDerivationRequest("कृ", Samjna.KTA))
        assertEquals("कृत", res2.final.surface)
    }

    @Test
    fun `nvul trc and ghan generate bhavaka kartr and bhava`() {
        val res1 = engine.derive(KrdantaDerivationRequest("भू", Samjna.NVUL))
        assertEquals("भावक", res1.final.surface)
        assertTrue(res1.applications.any { it.sutra == "3.1.133" })

        val res2 = engine.derive(KrdantaDerivationRequest("कृ", Samjna.TRC))
        assertEquals("कर्तृ", res2.final.surface)

        val res3 = engine.derive(KrdantaDerivationRequest("भू", Samjna.GHAN))
        assertEquals("भाव", res3.final.surface)
        assertTrue(res3.applications.any { it.sutra == "3.3.18" })
    }

    @Test
    fun `krdanta provenance contains only explicitly staged rules`() {
        val requests = listOf(
            KrdantaDerivationRequest("कृ", Samjna.KTVA, upasarga = "अनु"),
            KrdantaDerivationRequest("भू", Samjna.TUMUN),
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
