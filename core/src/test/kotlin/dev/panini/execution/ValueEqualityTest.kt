package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class ValueEqualityTest {
    @Test
    fun `rendering does not define numeric or boolean equality`() {
        assertTrue(SanskritValue.Sankhya(2, "द्वि").semanticallyEquals(SanskritValue.Sankhya(2, "द्वे")))
        assertTrue(SanskritValue.Satya(true, "सत्य").semanticallyEquals(SanskritValue.Satya(true)))
        assertFalse(SanskritValue.Sankhya(2, "द्वि").semanticallyEquals(SanskritValue.Shabda("द्वि")))
    }

    @Test
    fun `record equality compares fields rather than the displayed schema name`() {
        val first = SanskritValue.Rupa("बिन्दु", mapOf("मान" to SanskritValue.Sankhya(1, "एक")))
        assertFalse(first.semanticallyEquals(first.copy(fields = mapOf("मान" to SanskritValue.Sankhya(2, "द्वि")))))
        assertTrue(first.semanticallyEquals(first.copy(fields = mapOf("मान" to SanskritValue.Sankhya(1, "एकम्")))))
        assertFalse(first.semanticallyEquals(first.copy(schema = "अन्य")))
    }

    @Test
    fun `collection comparison preserves order length and value kind`() {
        val first = SanskritValue.Suchi(listOf(SanskritValue.Sankhya(1, "एक"), SanskritValue.Sankhya(2, "द्वि")))
        assertTrue(first.semanticallyEquals(first.copy(items = listOf(SanskritValue.Sankhya(1, "एकम्"), SanskritValue.Sankhya(2, "द्वे")))))
        assertFalse(first.semanticallyEquals(first.copy(items = first.items.reversed())))
        assertFalse(first.semanticallyEquals(first.copy(items = first.items.take(1))))
        assertFalse(first.semanticallyEquals(SanskritValue.Gana(first.items)))
    }

    @Test
    fun `rational equality does not overflow cross multiplication`() {
        assertTrue(SanskritValue.Rational(Long.MAX_VALUE, Long.MAX_VALUE, "a")
            .semanticallyEquals(SanskritValue.Rational(1, 1, "b")))
        assertFalse(SanskritValue.Rational(1, 0, "a").semanticallyEquals(SanskritValue.Rational(2, 0, "b")))
    }
}
