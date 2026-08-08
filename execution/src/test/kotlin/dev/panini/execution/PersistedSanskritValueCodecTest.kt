package dev.panini.execution

import dev.panini.shiksha.Samjna
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import kotlin.test.Test
import kotlin.test.assertEquals

class PersistedSanskritValueCodecTest {

    @Test
    fun `round trips every persisted value shape`() {
        val values = listOf(
            SanskritValue.Sankhya(5, "पञ्च"),
            SanskritValue.Rational(1, 2, "अर्ध"),
            SanskritValue.Shabda("विशेष", setOf(Samjna.Rudhi("विशेष"))),
            SanskritValue.Gana(listOf(SanskritValue.Satya(true))),
            SanskritValue.Suchi(listOf(SanskritValue.Lopa)),
            SanskritValue.Satya(false),
            SanskritValue.Lopa,
        )

        values.forEach { assertEquals(it, decode(encode(it))) }
    }

    @Test
    fun `preserves established binary type tags`() {
        val values = listOf(
            SanskritValue.Sankhya(1, "एक"),
            SanskritValue.Rational(1, 2, "अर्ध"),
            SanskritValue.Shabda("शब्द"),
            SanskritValue.Gana(emptyList()),
            SanskritValue.Suchi(emptyList()),
            SanskritValue.Satya(true),
            SanskritValue.Lopa,
        )
        assertEquals((1..7).toList(), values.map { encode(it).first().toInt() })
    }

    private fun encode(value: SanskritValue): ByteArray = ByteArrayOutputStream().also { bytes ->
        DataOutputStream(bytes).use { PersistedSanskritValueCodec.write(it, value) }
    }.toByteArray()

    private fun decode(bytes: ByteArray): SanskritValue =
        DataInputStream(ByteArrayInputStream(bytes)).use(PersistedSanskritValueCodec::read)
}
