package dev.panini.aryabhatiya

import kotlin.test.Test
import kotlin.test.assertEquals

class AryabhatiyaDecoderTest {

    private val decoder = AryabhatiyaDecoder()

    @Test
    fun `decodes Gi to 300`() {
        // ग (Varga 3) + इ (vowel power 1 -> multiplier 10^2 = 100) -> 300
        val value = decoder.decode("गि")
        assertEquals(300L, value)
    }

    @Test
    fun `decodes Chayi to 3600`() {
        // च (Varga 6) + य (Avarga 30) + इ (power 1 -> multiplier 100)
        // च = 6 * 100 = 600
        // य = 30 * 100 = 3000
        // total = 3600
        val value = decoder.decode("चयि")
        assertEquals(3600L, value)
    }

    @Test
    fun `decodes Khyu`() {
        // ख (Varga 2) + य (Avarga 30) + उ (power 2 -> multiplier 10000)
        // ख = 2 * 10000 = 20000
        // य = 30 * 10000 = 300000
        // total = 320000
        val value = decoder.decode("ख्यु")
        assertEquals(320000L, value)
    }
}
