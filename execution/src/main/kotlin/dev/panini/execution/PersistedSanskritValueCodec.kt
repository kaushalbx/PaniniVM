package dev.panini.execution

import java.io.DataInputStream
import java.io.DataOutputStream

/** Binary value codec shared by V2 conversation state and kriyā memory. */
internal object PersistedSanskritValueCodec {
    fun write(output: DataOutputStream, value: SanskritValue) {
        with(output) {
            when (value) {
                is SanskritValue.Sankhya -> { writeByte(1); writeLong(value.value); writeUTF(value.word) }
                is SanskritValue.Rational -> {
                    writeByte(2); writeLong(value.numerator); writeLong(value.denominator); writeUTF(value.word)
                }
                is SanskritValue.Shabda -> {
                    writeByte(3); writeUTF(value.text); writeInt(value.samjnas.size)
                    value.samjnas.forEach { writeUTF(PersistedSamjnaCodec.encode(it)) }
                }
                is SanskritValue.Gana -> {
                    writeByte(4); writeInt(value.elements.size); value.elements.forEach { write(output, it) }
                }
                is SanskritValue.Suchi -> {
                    writeByte(5); writeInt(value.items.size); value.items.forEach { write(output, it) }
                }
                is SanskritValue.Satya -> { writeByte(6); writeBoolean(value.boolean) }
                SanskritValue.Lopa -> writeByte(7)
            }
        }
    }

    fun read(input: DataInputStream): SanskritValue = with(input) {
        when (val tag = readByte().toInt()) {
            1 -> SanskritValue.Sankhya(readLong(), readUTF())
            2 -> SanskritValue.Rational(readLong(), readLong(), readUTF())
            3 -> SanskritValue.Shabda(
                readUTF(),
                buildSet { repeat(readInt()) { add(PersistedSamjnaCodec.decode(readUTF())) } },
            )
            4 -> SanskritValue.Gana(buildList { repeat(readInt()) { add(read(input)) } })
            5 -> SanskritValue.Suchi(buildList { repeat(readInt()) { add(read(input)) } })
            6 -> SanskritValue.Satya(readBoolean())
            7 -> SanskritValue.Lopa
            else -> throw IllegalArgumentException("Unknown persisted Sanskrit value type: $tag")
        }
    }
}
