package dev.sanskrit.dhatupatha

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class DhatuPathaTest {
    @Test
    fun `complete dhatupatha retains source order and meanings`() {
        assertEquals(2259, DhatuPatha.all.size)
        assertEquals(1166, BhvadiDhatus.all.size)
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
        assertEquals(2259, DhatuPatha.all.size)
        assertEquals("भू", DhatuPatha.find("01.0001")?.upadesha)
        assertEquals(dev.sanskrit.shiksha.ItStatus.SET, DhatuPatha.find("01.0001")?.itStatus)
        assertEquals(dev.sanskrit.shiksha.Karmatva.AKARMAKA, DhatuPatha.find("01.0001")?.karmatva)
        assertEquals(dev.sanskrit.shiksha.Accent.UDATTA, DhatuPatha.find("01.0001")?.svara)

        val annotatedDhatu = assertNotNull(DhatuPatha.find("02.0001"))
        assertEquals("अदँ", annotatedDhatu.upadesha)
        assertEquals(dev.sanskrit.shiksha.ItStatus.ANIT, annotatedDhatu.itStatus)
        assertEquals(dev.sanskrit.shiksha.Karmatva.SAKARMAKA, annotatedDhatu.karmatva)

        val ganaSutra = assertNotNull(DhatuPatha.find("02.0076"))
        assertEquals("-", ganaSutra.upadesha)
        assertEquals(null, ganaSutra.itStatus)
    }
}
