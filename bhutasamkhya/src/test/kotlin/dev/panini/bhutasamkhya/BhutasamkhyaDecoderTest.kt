package dev.panini.bhutasamkhya

import kotlin.test.Test
import kotlin.test.assertEquals

class BhutasamkhyaDecoderTest {

    private val decoder = BhutasamkhyaDecoder()

    @Test
    fun `decodes Netra Veda to 42`() {
        // नेत्र (2), वेद (4) -> digits [2, 4] -> reversed [4, 2] -> 42
        val value = decoder.decodeTerms(listOf("नेत्र", "वेद"))
        assertEquals(42L, value)
    }

    @Test
    fun `decodes Agni Bana to 53`() {
        // अग्नि (3), बाण (5) -> digits [3, 5] -> reversed [5, 3] -> 53
        val value = decoder.decodeTerms(listOf("अग्नि", "बाण"))
        assertEquals(53L, value)
    }

    @Test
    fun `decodes hyphenated Bhutasamkhya text`() {
        val value = decoder.decode("नेत्र-वेद")
        assertEquals(42L, value)
    }
}
