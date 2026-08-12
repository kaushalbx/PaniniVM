package dev.panini.derivation

import dev.panini.core.Lakara
import dev.panini.core.PadaType
import dev.panini.core.TingAffix
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Dedicated test suite verifying Ātmanepada & Ubhayapada Tiṅanta verb paradigms.
 */
class AtmanepadaTingantaTest {

    private val engine = TingantaEngine()

    @Test
    fun `test labhate atmanepada present paradigm`() {
        val paradigm = engine.deriveSupportedParadigm("डुलभँष्", pada = PadaType.ATMANEPADA, lakara = Lakara.LAT)
        val surfaces = paradigm.derivationSurfaces

        assertEquals("लभते", surfaces[TingAffix.TA])
        assertEquals("लभेते", surfaces[TingAffix.ATAM])
        assertEquals("लभन्ते", surfaces[TingAffix.JHA])
        assertEquals("लभसे", surfaces[TingAffix.THAS_A])
        assertEquals("लभेथे", surfaces[TingAffix.ATHAM])
        assertEquals("लभध्वे", surfaces[TingAffix.DHVAM])
        assertEquals("लभे", surfaces[TingAffix.IT])
        assertEquals("लभावहे", surfaces[TingAffix.VAHI])
        assertEquals("लभामहे", surfaces[TingAffix.MAHING])
    }

    @Test
    fun `test sevate atmanepada present paradigm`() {
        val paradigm = engine.deriveSupportedParadigm("षेवृँ", pada = PadaType.ATMANEPADA, lakara = Lakara.LAT)
        val surfaces = paradigm.derivationSurfaces

        assertEquals("सेवते", surfaces[TingAffix.TA])
        assertEquals("सेवेते", surfaces[TingAffix.ATAM])
        assertEquals("सेवन्ते", surfaces[TingAffix.JHA])
        assertEquals("सेवसे", surfaces[TingAffix.THAS_A])
        assertEquals("सेवेथे", surfaces[TingAffix.ATHAM])
        assertEquals("सेवध्वे", surfaces[TingAffix.DHVAM])
        assertEquals("सेवे", surfaces[TingAffix.IT])
        assertEquals("सेवावहे", surfaces[TingAffix.VAHI])
        assertEquals("सेवामहे", surfaces[TingAffix.MAHING])
    }

    @Test
    fun `test edhate atmanepada present paradigm`() {
        val paradigm = engine.deriveSupportedParadigm("एधँ", pada = PadaType.ATMANEPADA, lakara = Lakara.LAT)
        val surfaces = paradigm.derivationSurfaces

        assertEquals("एधते", surfaces[TingAffix.TA])
        assertEquals("एधेते", surfaces[TingAffix.ATAM])
        assertEquals("एधन्ते", surfaces[TingAffix.JHA])
        assertEquals("एधसे", surfaces[TingAffix.THAS_A])
        assertEquals("एधेथे", surfaces[TingAffix.ATHAM])
        assertEquals("एधध्वे", surfaces[TingAffix.DHVAM])
        assertEquals("एधे", surfaces[TingAffix.IT])
        assertEquals("एधावहे", surfaces[TingAffix.VAHI])
        assertEquals("एधामहे", surfaces[TingAffix.MAHING])
    }

    @Test
    fun `test yajati and yajate ubhayapada present paradigms`() {
        val parasmaipada = engine.deriveSupportedParadigm("यजँ", pada = PadaType.PARASMAIPADA, lakara = Lakara.LAT)
        val atmanepada = engine.deriveSupportedParadigm("यजँ", pada = PadaType.ATMANEPADA, lakara = Lakara.LAT)

        assertEquals("यजति", parasmaipada.derivationSurfaces[TingAffix.TIP])
        assertEquals("यजते", atmanepada.derivationSurfaces[TingAffix.TA])
    }
}
