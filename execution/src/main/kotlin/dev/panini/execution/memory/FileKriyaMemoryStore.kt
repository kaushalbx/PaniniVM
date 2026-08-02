package dev.panini.execution.memory

import dev.panini.analysis.KriyaFrame
import dev.panini.analysis.KriyaId
import dev.panini.execution.SanskritValue
import dev.panini.shiksha.Samjna
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/** Compact persistent form for Kriyā memory using binary V2 encoding. */
@OptIn(ExperimentalEncodingApi::class)
internal class FileKriyaMemoryStore(private val storageDir: File) {
    private val analysisCache = java.util.concurrent.ConcurrentHashMap<String, List<KriyaFrame>>()

    fun save(key: String, memory: KriyaMemory) {
        val bytesStream = ByteArrayOutputStream()
        DataOutputStream(bytesStream).use { out ->
            out.writeUTF(HEADER_V2)
            out.writeInt(memory.entries.size)
            for (entry in memory.entries) {
                val sourceText = entry.frame.vakya.padas.joinToString(" ") { it.sourceText }
                val normalizedSource = sourceText.trim().let { if (it.endsWith("।")) it else "$it ।" }
                analysisCache.putIfAbsent(normalizedSource, listOf(entry.frame))
                out.writeInt(entry.turn)
                out.writeUTF(entry.frame.id.value)
                out.writeUTF(sourceText)
                out.writeBoolean(entry.phala != null)
                entry.phala?.let { out.writeValue(it) }
            }
        }
        fileFor(key).writeBytes(bytesStream.toByteArray())
    }

    fun load(key: String, analyze: (String) -> List<KriyaFrame>): KriyaMemory? {
        val file = fileFor(key)
        if (!file.exists()) return null
        val bytes = file.readBytes()
        if (bytes.isEmpty()) return null

        val dis = DataInputStream(ByteArrayInputStream(bytes))
        val header = try { dis.readUTF() } catch (e: Exception) { return null }
        if (header != HEADER_V2) return null

        val count = dis.readInt()
        val entries = List(count) {
            val turn = dis.readInt()
            val idValue = dis.readUTF()
            val sourceText = dis.readUTF()
            val hasPhala = dis.readBoolean()
            val phala = if (hasPhala) dis.readValue() else null

            val source = sourceText.trim().let { if (it.endsWith("।")) it else "$it ।" }
            val frames = analysisCache.computeIfAbsent(source) { analyze(source) }
            val frame = frames.singleOrNull()
                ?: error("Cannot reconstruct persisted kriyā from: $source")
            RememberedKriya(
                turn = turn,
                frame = frame.withMemoryId(KriyaId(idValue)),
                phala = phala,
            )
        }
        return KriyaMemory(entries)
    }

    private fun fileFor(key: String): File = File(storageDir, encode(key) + EXTENSION)

    private fun DataOutputStream.writeValue(value: SanskritValue) {
        when (value) {
            is SanskritValue.Sankhya -> { writeByte(1); writeLong(value.value); writeUTF(value.word) }
            is SanskritValue.Rational -> {
                writeByte(2); writeLong(value.numerator); writeLong(value.denominator); writeUTF(value.word)
            }
            is SanskritValue.Shabda -> {
                writeByte(3); writeUTF(value.text); writeInt(value.samjnas.size)
                value.samjnas.forEach { writeUTF(encodeSamjna(it)) }
            }
            is SanskritValue.Gana -> {
                writeByte(4); writeInt(value.elements.size); value.elements.forEach { writeValue(it) }
            }
            is SanskritValue.Suchi -> {
                writeByte(5); writeInt(value.items.size); value.items.forEach { writeValue(it) }
            }
            is SanskritValue.Satya -> { writeByte(6); writeBoolean(value.boolean) }
        }
    }

    private fun DataInputStream.readValue(): SanskritValue = when (readByte().toInt()) {
        1 -> SanskritValue.Sankhya(readLong(), readUTF())
        2 -> SanskritValue.Rational(readLong(), readLong(), readUTF())
        3 -> SanskritValue.Shabda(readUTF(), buildSet { repeat(readInt()) { add(decodeSamjna(readUTF())) } })
        4 -> SanskritValue.Gana(List(readInt()) { readValue() })
        5 -> SanskritValue.Suchi(List(readInt()) { readValue() })
        6 -> SanskritValue.Satya(readBoolean())
        else -> error("Unknown persisted Sanskrit value type.")
    }

    private fun encodeSamjna(samjna: Samjna): String = when (samjna) {
        is Samjna.Rudhi -> "RUDHI:${samjna.word}"
        is Enum<*> -> samjna.name
    }

    private fun decodeSamjna(value: String): Samjna =
        if (value.startsWith("RUDHI:")) Samjna.Rudhi(value.substringAfter(':')) else Samjna.valueOf(value)

    private fun encode(value: String): String =
        if (value.isEmpty()) "" else base64Codec.encode(value.toByteArray(Charsets.UTF_8))

    private companion object {
        const val HEADER_V2 = "PANINI_KRIYA_MEMORY_V2"
        const val EXTENSION = ".kriya-memory"
        val base64Codec = Base64.UrlSafe.withPadding(Base64.PaddingOption.PRESENT_OPTIONAL)
    }
}
