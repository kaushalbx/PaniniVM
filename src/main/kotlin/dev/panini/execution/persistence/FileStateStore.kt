package dev.panini.execution.persistence

import dev.panini.execution.ExecutionSamjna
import dev.panini.execution.SambhashanaContext
import dev.panini.execution.SanskritValue
import dev.panini.execution.SmrtaPhala
import java.io.File
import java.nio.charset.StandardCharsets
import java.util.Base64

/** Versioned file store that round-trips complete conversation state. */
class FileStateStore(private val storageDir: File) : StateStore {
    init {
        require(storageDir.exists() || storageDir.mkdirs()) { "Cannot create state directory: $storageDir" }
        require(storageDir.isDirectory) { "State path is not a directory: $storageDir" }
    }

    override fun save(key: String, context: SambhashanaContext) {
        val lines = mutableListOf("PANINI_STATE_V2")
        lines += record("CONTEXT", context.speaker, context.listener, context.turnNumber.toString())
        context.mentionedEntities.forEach { (name, value) ->
            lines += record("ENTITY", name, value, encodeSamjnas(context.mentionedEntitySamjnas[name].orEmpty()))
        }
        context.previousResults.forEach { (name, value) ->
            lines += record(
                "RESULT", name, value, encodeSamjnas(context.previousResultSamjnas[name].orEmpty()),
                encodeTyped(context.previousTypedResults[name]),
            )
        }
        context.resultHistory.forEach { result ->
            lines += record(
                "HISTORY", result.id, result.turnNumber.toString(), result.invocationId,
                result.value, encodeSamjnas(result.samjnas), encodeTyped(result.typedValue),
            )
        }
        context.metadata.forEach { (name, value) -> lines += record("META", name, value) }
        fileFor(key).writeText(lines.joinToString("\n", postfix = "\n"))
    }

    override fun load(key: String): SambhashanaContext? {
        val file = fileFor(key)
        if (!file.exists()) return null
        val lines = file.readLines()
        if (lines.firstOrNull() != "PANINI_STATE_V2") return null

        var speaker = "प्रयोक्ता"
        var listener = "यन्त्रम्"
        var turnNumber = 0
        val entities = linkedMapOf<String, String>()
        val entitySamjnas = linkedMapOf<String, Set<ExecutionSamjna>>()
        val results = linkedMapOf<String, String>()
        val resultSamjnas = linkedMapOf<String, Set<ExecutionSamjna>>()
        val typedResults = linkedMapOf<String, SanskritValue>()
        val history = mutableListOf<SmrtaPhala>()
        val metadata = linkedMapOf<String, String>()

        lines.drop(1).filter(String::isNotBlank).forEach { line ->
            val fields = decodeRecord(line)
            when (fields.firstOrNull()) {
                "CONTEXT" -> {
                    speaker = fields[1]; listener = fields[2]; turnNumber = fields[3].toInt()
                }
                "ENTITY" -> {
                    entities[fields[1]] = fields[2]; entitySamjnas[fields[1]] = decodeSamjnas(fields[3])
                }
                "RESULT" -> {
                    val name = fields[1]
                    results[name] = fields[2]
                    resultSamjnas[name] = decodeSamjnas(fields[3])
                    decodeTyped(fields[4], fields[2], resultSamjnas.getValue(name))?.let { typedResults[name] = it }
                }
                "HISTORY" -> history += SmrtaPhala(
                    id = fields[1], turnNumber = fields[2].toInt(), invocationId = fields[3],
                    value = fields[4], samjnas = decodeSamjnas(fields[5]),
                    typedValue = decodeTyped(fields[6], fields[4], decodeSamjnas(fields[5])),
                )
                "META" -> metadata[fields[1]] = fields[2]
            }
        }
        return SambhashanaContext(
            speaker, listener, entities, entitySamjnas, results, resultSamjnas,
            typedResults, history, turnNumber, metadata,
        )
    }

    override fun delete(key: String): Boolean = fileFor(key).let { it.exists() && it.delete() }

    override fun listKeys(): List<String> = storageDir.listFiles { _, name -> name.endsWith(EXTENSION) }
        ?.mapNotNull { file ->
            runCatching { decode(file.name.removeSuffix(EXTENSION)) }.getOrNull()
        }.orEmpty()

    private fun fileFor(key: String): File {
        val safe = Base64.getUrlEncoder().withoutPadding().encodeToString(key.toByteArray(StandardCharsets.UTF_8))
        return File(storageDir, safe + EXTENSION)
    }

    private fun record(type: String, vararg values: String): String =
        (listOf(type) + values.map(::encode)).joinToString("\t")

    private fun decodeRecord(line: String): List<String> = line.split('\t').mapIndexed { index, field ->
        if (index == 0) field else decode(field)
    }

    private fun encode(value: String): String =
        Base64.getUrlEncoder().withoutPadding().encodeToString(value.toByteArray(StandardCharsets.UTF_8))

    private fun decode(value: String): String =
        String(Base64.getUrlDecoder().decode(value), StandardCharsets.UTF_8)

    private fun encodeSamjnas(values: Set<ExecutionSamjna>): String = values.joinToString(",") { it.name }
    private fun decodeSamjnas(value: String): Set<ExecutionSamjna> = value.split(',').filter(String::isNotEmpty)
        .mapTo(mutableSetOf(), ExecutionSamjna::valueOf)

    private fun encodeTyped(value: SanskritValue?): String = when (value) {
        is SanskritValue.Sankhya -> "SANKHYA:${value.value}"
        is SanskritValue.Satya -> "SATYA:${value.boolean}"
        is SanskritValue.Shabda -> "SHABDA"
        is SanskritValue.Gana -> "GANA"
        null -> ""
    }

    private fun decodeTyped(type: String, display: String, samjnas: Set<ExecutionSamjna>): SanskritValue? = when {
        type.startsWith("SANKHYA:") -> SanskritValue.Sankhya(type.substringAfter(':').toLong(), display)
        type.startsWith("SATYA:") -> SanskritValue.Satya(type.substringAfter(':').toBooleanStrict())
        type == "SHABDA" || type == "GANA" -> SanskritValue.Shabda(display, samjnas)
        else -> null
    }

    private companion object { const val EXTENSION = ".state" }
}
