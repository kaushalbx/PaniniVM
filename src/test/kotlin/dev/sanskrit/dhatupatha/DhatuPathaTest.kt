package dev.sanskrit.dhatupatha

import kotlin.test.Test
import kotlin.test.assertEquals

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
}
