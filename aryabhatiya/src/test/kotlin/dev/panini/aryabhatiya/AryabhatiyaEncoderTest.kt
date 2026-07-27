package dev.panini.aryabhatiya

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AryabhatiyaEncoderTest {

    private val encoder = AryabhatiyaEncoder()
    private val decoder = AryabhatiyaDecoder()

    @Test
    fun `encodes Gi to 300`() {
        assertEquals("गि", encoder.encode(300L))
    }

    @Test
    fun `encodes Chayi to 3600`() {
        assertEquals("च्यि", encoder.encode(3600L))
    }

    @Test
    fun `encodes Khyu to 320000`() {
        assertEquals("ख्यु", encoder.encode(320000L))
    }

    @Test
    fun `encodes 26 to Nca`() {
        assertEquals("न्च", encoder.encode(26L))
    }

    @Test
    fun `encodes power 4 correctly`() {
        // ग (3) * 10^8 = 300,000,000
        assertEquals("गॢ", encoder.encode(300_000_000L))
    }

    @Test
    fun `encodes multi-syllable numbers`() {
        // 320300 -> गि (300) + khyu (320,000)
        assertEquals("गिख्यु", encoder.encode(320300L))
    }

    @Test
    fun `throws on non-positive numbers`() {
        assertFailsWith<IllegalArgumentException> {
            encoder.encode(0L)
        }
        assertFailsWith<IllegalArgumentException> {
            encoder.encode(-15L)
        }
    }

    @Test
    fun `throws on numbers too large`() {
        assertFailsWith<IllegalArgumentException> {
            encoder.encode(100_000_000_000_000L) // 10^14
        }
    }

    @Test
    fun `round-trip validation`() {
        val testValues = listOf(
            1L, 2L, 5L, 10L, 20L, 25L, 26L, 29L, 30L, 32L, 35L, 90L, 99L,
            100L, 101L, 125L, 126L, 130L, 200L, 300L, 3600L, 320000L, 320300L,
            1_000_000L, 3_000_000L, 300_000_000L, 123456789L, 99999999999999L
        )
        for (value in testValues) {
            val encoded = encoder.encode(value)
            val decoded = decoder.decode(encoded)
            assertEquals(value, decoded, "Roundtrip failed for $value (encoded: $encoded)")
        }
    }
}
