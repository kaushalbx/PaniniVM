package dev.panini.katapayadi

import kotlin.test.Test
import kotlin.test.assertEquals

class KatapayadiDecoderTest {

    private val decoder = KatapayadiDecoder()

    @Test
    fun `decodes Madhava to 495`() {
        // म (5), ध (9), व (4) -> digits [5, 9, 4] -> reversed [4, 9, 5] -> 495
        val value = decoder.decode("माधव")
        assertEquals(495L, value)
    }

    @Test
    fun `decodes Khaga to 32`() {
        // ख (2), ग (3) -> digits [2, 3] -> reversed [3, 2] -> 32
        val value = decoder.decode("खग")
        assertEquals(32L, value)
    }

    @Test
    fun `decodes Katapayadi string`() {
        // क (1), ट (1), प (1), य (1), द (8) -> digits [1, 1, 1, 1, 8] -> reversed [8, 1, 1, 1, 1] -> 81111
        val value = decoder.decode("कटपयादि")
        assertEquals(81111L, value)
    }

    @Test
    fun `handles conjunct consonants by taking final consonant`() {
        // ख्य (य = 1), ग (3) -> digits [1, 3] -> reversed [3, 1] -> 31
        val value = decoder.decode("ख्याग")
        assertEquals(31L, value)
    }
}
