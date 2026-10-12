package dev.panini.execution

import dev.panini.shiksha.Samjna
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertFailsWith

class LegacySanskritValueCodecTest {
    @Test fun `legacy fractions cannot silently discard extra numeric components`() {
        for (tag in listOf("RATIONAL:1/2/3", "RATIONAL:1", "RATIONAL:/2", "RATIONAL:1/")) {
            assertFailsWith<IllegalArgumentException>(tag) { decode(tag, "fraction") }
        }
        assertEquals(SanskritValue.Rational(1, -2, "negative"), decode("RATIONAL:1/-2", "negative"))
    }


    @Test
    fun `decodes legacy scalar value tags`() {
        assertEquals(SanskritValue.Lopa, decode("LOPA", "लोपः"))
        assertEquals(SanskritValue.Sankhya(5, "पञ्च"), decode("SANKHYA:5", "पञ्च"))
        assertEquals(SanskritValue.Rational(1, 2, "अर्ध"), decode("RATIONAL:1/2", "अर्ध"))
        assertEquals(SanskritValue.Satya(true), decode("SATYA:true", "सत्यम्"))
    }

    @Test
    fun `preserves legacy textual tags and rejects unknown tags`() {
        val samjnas = setOf(Samjna.SHABDA)
        assertEquals(SanskritValue.Shabda("राम", samjnas), decode("SHABDA", "राम", samjnas))
        assertEquals(SanskritValue.Shabda("एक द्वि", samjnas), decode("GANA", "एक द्वि", samjnas))
        assertNull(decode("UNKNOWN", "राम", samjnas))
    }

    private fun decode(
        type: String,
        display: String,
        samjnas: Set<Samjna> = emptySet(),
    ): SanskritValue? = LegacySanskritValueCodec.decode(type, display, samjnas)
}
