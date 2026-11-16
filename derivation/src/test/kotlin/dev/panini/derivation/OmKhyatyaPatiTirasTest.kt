package dev.panini.derivation

import dev.panini.core.Linga
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class OmKhyatyaPatiTirasTest {
    private val engine = SandhiEngine()
    private fun all(left: String, right: String) = engine.joinAll(left, right).withoutIndependentOptions().map(engine::render).toSet()
    private fun form(stem: String, vibhakti: Vibhakti) =
        SubantaEngine().derive(SubantaDerivationRequest(stem, vibhakti, Vacana.EKAVACANA, Linga.PUMS)).final.surface

    @Test fun `6_1_95 takes pararupa before om`() {
        assertEquals(setOf("शिवोम्"), all("शिव", "ओम्"))
        // Only the indeclinable ओम् by upadeśa: an ordinary ओ-initial word keeps vṛddhi.
        assertEquals(setOf("शिवौदनम्"), all("शिव", "ओदनम्"))
    }

    @Test fun `6_1_112 turns the a of ngasi and ngas into u after khya and tya`() {
        assertEquals("सख्युः", form("सखि", Vibhakti.SASTHI))
        assertEquals("सख्युः", form("सखि", Vibhakti.PANCHAMI))
    }

    @Test fun `1_4_8 makes pati ghi only in a compound`() {
        assertEquals("पत्युः", form("पति", Vibhakti.SASTHI))
        assertEquals("पत्ये", form("पति", Vibhakti.CHATURTHI))
        // An ordinary ghi stem keeps guṇa.
        assertEquals("हरेः", form("हरि", Vibhakti.SASTHI))
    }

    @Test fun `8_3_42 optionally gives s for the visarga of tiras`() {
        val forms = all("तिरस्", "कृत्वा")
        assertTrue("तिरस्कृत्वा" in forms && "तिरःकृत्वा" in forms, forms.toString())
        // A word given already with visarga has no evidence of its own स्.
        assertTrue(all("तिरः", "कृत्वा").none { "तिरस्" in it })
    }
}
