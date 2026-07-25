package dev.panini.dhatupatha

import dev.panini.core.PadaType
import dev.panini.dhatupatha.adadi.AdadiDhatus
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.curadi.CuradiDhatus
import dev.panini.dhatupatha.divadi.DivadiDhatus
import dev.panini.dhatupatha.juhotyadi.JuhotyadiDhatus
import dev.panini.dhatupatha.kryadi.KryadiDhatus
import dev.panini.dhatupatha.rudhadi.RudhadiDhatus
import dev.panini.dhatupatha.svadi.SvadiDhatus
import dev.panini.dhatupatha.tanadi.TanadiDhatus
import dev.panini.dhatupatha.tudadi.TudadiDhatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class DhatuPathaTest {
    @Test
    fun `complete dhatupatha retains source order and meanings`() {
        assertEquals(2264, DhatuPatha.all.size)
        assertEquals(1171, BhvadiDhatus.all.size)
        assertEquals(listOf(1, 2, 3), BhvadiDhatus.all.take(3).map { it.krama })
        assertEquals("सत्तायाम्", DhatuPatha.find("01.0001")?.artha)
        assertEquals("होना", DhatuPatha.find("01.0001")?.arthaHindi)
        assertEquals("to exist, to become, to be, to happen", DhatuPatha.find("01.0001")?.arthaEnglish)
        assertEquals(
            PadaType.PARASMAIPADA,
            DhatuPatha.findByUpadesha("भू").first { it.id == "01.0001" }.pada,
        )
        assertEquals(77, AdadiDhatus.all.size)
        assertEquals(26, JuhotyadiDhatus.all.size)
        assertEquals(163, DivadiDhatus.all.size)
        assertEquals(38, SvadiDhatus.all.size)
        assertEquals(174, TudadiDhatus.all.size)
        assertEquals(25, RudhadiDhatus.all.size)
        assertEquals(10, TanadiDhatus.all.size)
        assertEquals(71, KryadiDhatus.all.size)
        assertEquals(509, CuradiDhatus.all.size)
    }

    @Test
    fun `all entries expose canonical upadesha after the metadata extension`() {
        assertEquals("भू", DhatuPatha.find("01.0001")?.upadesha)
        assertEquals(dev.panini.shiksha.ItStatus.SET, DhatuPatha.find("01.0001")?.itStatus)
        assertEquals(dev.panini.shiksha.Karmatva.AKARMAKA, DhatuPatha.find("01.0001")?.karmatva)
        assertEquals(dev.panini.shiksha.Accent.UDATTA, DhatuPatha.find("01.0001")?.svara)

        val annotatedDhatu = assertNotNull(DhatuPatha.find("02.0001"))
        assertEquals("अदँ", annotatedDhatu.upadesha)
        assertEquals(dev.panini.shiksha.ItStatus.ANIT, annotatedDhatu.itStatus)
        assertEquals(dev.panini.shiksha.Karmatva.SAKARMAKA, annotatedDhatu.karmatva)

        val ganaSutra = assertNotNull(DhatuPatha.find("02.0076"))
        assertEquals("-", ganaSutra.upadesha)
        assertEquals(null, ganaSutra.itStatus)
    }
}
