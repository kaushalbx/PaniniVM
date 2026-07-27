package dev.panini.bhutasamkhya

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class BhutasamkhyaEncoderTest {

    private val encoder = BhutasamkhyaEncoder()
    private val decoder = BhutasamkhyaDecoder()

    @Test
    fun `encodes zero to Shunya`() {
        assertEquals("शून्य", encoder.encode(0L))
    }

    @Test
    fun `encodes 42 to Netra Veda`() {
        assertEquals("नेत्र-वेद", encoder.encode(42L))
    }

    @Test
    fun `encodes 53 to Rama Bana`() {
        assertEquals("राम-बाण", encoder.encode(53L))
    }

    @Test
    fun `encodes direct lexicon terms`() {
        assertEquals("दिक्", encoder.encode(10L))
        assertEquals("सूर्य", encoder.encode(12L))
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
            0L, 1L, 2L, 5L, 9L, 10L, 11L, 12L, 32L, 42L, 53L, 1234567890L, 99999999999L
        )
        for (value in testValues) {
            val encoded = encoder.encode(value)
            val decoded = decoder.decode(encoded)
            assertEquals(value, decoded, "Roundtrip failed for $value (encoded: $encoded)")
        }
    }
}
