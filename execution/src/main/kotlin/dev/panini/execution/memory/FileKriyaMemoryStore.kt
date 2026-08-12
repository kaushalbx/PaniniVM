package dev.panini.execution.memory

import dev.panini.analysis.KriyaFrame
import dev.panini.analysis.KriyaId
import dev.panini.execution.PersistedSanskritValueCodec
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
                val normalizedSource = KriyaMemorySource.normalize(sourceText)
                analysisCache.putIfAbsent(normalizedSource, listOf(entry.frame))
                out.writeInt(entry.turn)
                out.writeUTF(entry.frame.id.value)
                out.writeUTF(sourceText)
                out.writeBoolean(entry.phala != null)
                entry.phala?.let { PersistedSanskritValueCodec.write(out, it) }
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
            val phala = if (hasPhala) PersistedSanskritValueCodec.read(dis) else null

            val source = KriyaMemorySource.normalize(sourceText)
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

    fun delete(key: String): Boolean = fileFor(key).let { !it.exists() || it.delete() }

    private fun fileFor(key: String): File = File(storageDir, encode(key) + EXTENSION)

    private fun encode(value: String): String =
        if (value.isEmpty()) "" else base64Codec.encode(value.toByteArray(Charsets.UTF_8))

    private companion object {
        const val HEADER_V2 = "PANINI_KRIYA_MEMORY_V2"
        const val EXTENSION = ".kriya-memory"
        val base64Codec = Base64.UrlSafe.withPadding(Base64.PaddingOption.PRESENT_OPTIONAL)
    }
}
