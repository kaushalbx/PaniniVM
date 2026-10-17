package dev.panini.execution

import dev.panini.shiksha.Samjna
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PersistedSanskritValueCodecTest {
    @Test fun `generated fraction display and typed payload survive persistence`() {
        val value = sumNumericCollection(SanskritValue.Suchi(listOf(
            SanskritValue.Rational(1, 2, "अर्ध"), SanskritValue.Sankhya(1, "एक")))) { "number-$it" }!!
        val restored = decode(encode(value))
        assertEquals(value, restored)
        assertEquals("3/2", restored.toDisplayText())
    }

    @Test fun `negative collection counts cannot restore as empty values`() {
        for (tag in listOf(4, 5, 10)) {
            val bytes = ByteArrayOutputStream().also { buffer -> DataOutputStream(buffer).use {
                it.writeByte(tag)
                if (tag == 10) it.writeUTF("NUMBER")
                it.writeInt(-1)
            } }.toByteArray()
            assertFailsWith<IllegalArgumentException> { decode(bytes) }
        }
    }

    @Test fun `duplicate record fields cannot silently replace persisted values`() {
        val bytes = ByteArrayOutputStream().also { buffer -> DataOutputStream(buffer).use {
            it.writeByte(9); it.writeUTF("record"); it.writeInt(2)
            repeat(2) { index ->
                it.writeUTF("field")
                PersistedSanskritValueCodec.write(it, SanskritValue.Sankhya(index.toLong(), "number"))
            }
        } }.toByteArray()
        assertFailsWith<IllegalArgumentException> { decode(bytes) }
    }

    @Test fun `numeric list restoration validates fraction eligibility`() {
        val bytes = ByteArrayOutputStream().also { buffer -> DataOutputStream(buffer).use {
            it.writeByte(10); it.writeUTF("NUMBER"); it.writeInt(1)
            PersistedSanskritValueCodec.write(it, SanskritValue.Rational(1, 0, "undefined"))
        } }.toByteArray()
        assertFailsWith<IllegalArgumentException> { decode(bytes) }
    }

    @Test
    fun `round trips every persisted value shape`() {
        val values = listOf(
            SanskritValue.Purana(2, "द्वितीय"),
            SanskritValue.Rupa("ordinal", mapOf("position" to SanskritValue.Purana(3, "तृतीय"))),
            SanskritValue.Sankhya(5, "पञ्च"),
            SanskritValue.Rational(1, 2, "अर्ध"),
            SanskritValue.Suchi(listOf(SanskritValue.Rational(1, -2, "negative half")), ListMemberType.NUMBER),
            SanskritValue.Rupa("record", mapOf("field" to SanskritValue.Rational(1, 2, "अर्ध"))),
            SanskritValue.Range(SanskritValue.Sankhya(1, "एक"), SanskritValue.Sankhya(2, "द्वि")),
            SanskritValue.Shabda("विशेष", setOf(Samjna.Rudhi("विशेष"))),
            SanskritValue.Gana(listOf(SanskritValue.Satya(true))),
            SanskritValue.Suchi(listOf(SanskritValue.Lopa)),
            SanskritValue.Suchi(listOf(SanskritValue.Sankhya(1, "एक")), ListMemberType.NUMBER),
            SanskritValue.Suchi(emptyList(), ListMemberType.NUMBER),
            SanskritValue.Suchi(listOf(SanskritValue.Shabda("राम")), ListMemberType.TEXT),
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
