package dev.panini.execution.persistence

import dev.panini.execution.ExecutionSamjna
import dev.panini.execution.SambhashanaContext
import java.io.File

/**
 * File-backed implementation of StateStore.
 * Persists session context (entities, history) into key-value JSON files on disk.
 */
class FileStateStore(private val storageDir: File) : StateStore {

    init {
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }
    }

    override fun save(key: String, context: SambhashanaContext) {
        val file = getFileForKey(key)
        val sb = StringBuilder()
        sb.appendLine("{")
        sb.appendLine("  \"speaker\": \"${context.speaker}\",")
        sb.appendLine("  \"listener\": \"${context.listener}\",")

        // Entities
        sb.appendLine("  \"mentionedEntities\": {")
        val varEntries = context.mentionedEntities.entries.toList()
        varEntries.forEachIndexed { i, (k, v) ->
            val comma = if (i < varEntries.size - 1) "," else ""
            sb.appendLine("    \"$k\": \"$v\"$comma")
        }
        sb.appendLine("  },")

        // Previous results
        sb.appendLine("  \"previousResults\": {")
        val prevEntries = context.previousResults.entries.toList()
        prevEntries.forEachIndexed { i, (k, v) ->
            val comma = if (i < prevEntries.size - 1) "," else ""
            sb.appendLine("    \"$k\": \"$v\"$comma")
        }
        sb.appendLine("  }")
        sb.appendLine("}")

        file.writeText(sb.toString())
    }

    override fun load(key: String): SambhashanaContext? {
        val file = getFileForKey(key)
        if (!file.exists()) return null

        val json = file.readText()
        val speakerStr = extractJsonString(json, "speaker") ?: "प्रयोक्ता"
        val listenerStr = extractJsonString(json, "listener") ?: "यन्त्रम्"

        val entitiesMap = extractJsonMap(json, "mentionedEntities")
        val previousResultsMap = extractJsonMap(json, "previousResults")

        val entitySamjnas = entitiesMap.mapValues { (_, v) ->
            if (v.toLongOrNull() != null || dev.panini.execution.SanskritNumbers.valueOf(v) != null) {
                setOf(ExecutionSamjna.SANKHYA)
            } else {
                setOf(ExecutionSamjna.SHABDA)
            }
        }

        val previousSamjnas = previousResultsMap.mapValues { (_, v) ->
            if (v.toLongOrNull() != null || dev.panini.execution.SanskritNumbers.valueOf(v) != null) {
                setOf(ExecutionSamjna.SANKHYA)
            } else {
                setOf(ExecutionSamjna.SHABDA)
            }
        }

        return SambhashanaContext(
            speaker = speakerStr,
            listener = listenerStr,
            mentionedEntities = entitiesMap,
            mentionedEntitySamjnas = entitySamjnas,
            previousResults = LinkedHashMap(previousResultsMap),
            previousResultSamjnas = previousSamjnas,
        )
    }

    override fun delete(key: String): Boolean {
        val file = getFileForKey(key)
        return if (file.exists()) file.delete() else false
    }

    override fun listKeys(): List<String> {
        return storageDir.listFiles { _, name -> name.endsWith(".json") }
            ?.map { it.name.removeSuffix(".json") }
            ?: emptyList()
    }

    private fun getFileForKey(key: String): File {
        val sanitized = key.replace(Regex("[^a-zA-Z0-9_-]"), "_")
        return File(storageDir, "$sanitized.json")
    }

    private fun extractJsonString(json: String, key: String): String? {
        val regex = Regex("\"$key\"\\s*:\\s*\"([^\"]+)\"")
        return regex.find(json)?.groupValues?.get(1)
    }

    private fun extractJsonMap(json: String, sectionKey: String): Map<String, String> {
        val sectionRegex = Regex("\"$sectionKey\"\\s*:\\s*\\{([^}]*)\\}")
        val match = sectionRegex.find(json) ?: return emptyMap()
        val content = match.groupValues[1]

        val entryRegex = Regex("\"([^\"]+)\"\\s*:\\s*\"([^\"]+)\"")
        val result = mutableMapOf<String, String>()
        entryRegex.findAll(content).forEach { entry ->
            result[entry.groupValues[1]] = entry.groupValues[2]
        }
        return result
    }
}
