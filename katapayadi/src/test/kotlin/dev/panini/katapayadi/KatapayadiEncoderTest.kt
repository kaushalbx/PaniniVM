package dev.panini.katapayadi

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class KatapayadiEncoderTest {

    private val encoder = KatapayadiEncoder()
    private val decoder = KatapayadiDecoder()

    @Test
    fun `encodes zero to Nya`() {
        assertEquals("ञ", encoder.encode(0L))
    }

    @Test
    fun `encodes 32 to Khaga`() {
        // 32: digit 2 (units) -> 'ख', digit 3 (tens) -> 'ग'
        assertEquals("खग", encoder.encode(32L))
    }

    @Test
    fun `encodes 495 to Ngajhagha`() {
        // 495: digit 5 (units) -> 'ङ', digit 9 (tens) -> 'झ', digit 4 (hundreds) -> 'घ'
        assertEquals("ङझघ", encoder.encode(495L))
    }

    @Test
    fun `throws on negative numbers`() {
        assertFailsWith<IllegalArgumentException> {
            encoder.encode(-1L)
        }
        assertFailsWith<IllegalArgumentException> {
            encoder.encode(-999L)
        }
    }

    @Test
    fun `round-trip validation`() {
        val testValues = listOf(
            0L, 1L, 2L, 5L, 9L, 10L, 11L, 32L, 495L, 81111L, 1234567890L, 99999999999L
        )
        for (value in testValues) {
            val encoded = encoder.encode(value)
            val decoded = decoder.decode(encoded)
            assertEquals(value, decoded, "Roundtrip failed for $value (encoded: $encoded)")
        }
    }
}
