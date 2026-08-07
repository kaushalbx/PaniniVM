package dev.panini.execution

/**
 * A user-defined reusable kriyā, named via the संज्ञा-सूत्र pattern.
 */
data class SamjnaKriya(
    val nameSegmented: String,
    val nameStem: String,
    val body: List<PvmScriptStatement.Sentence>,
    val sourceFile: String? = null,
    val isApavada: Boolean = false,
    val isInternal: Boolean = false,
) {
    val nishedhaGuards: List<PvmScriptStatement.Sentence> = body.filter { it.isNishedha }
    val vidhiSentences: List<PvmScriptStatement.Sentence> = body.filterNot { it.isNishedha }
}

/**
 * Global registry of saṃjñā kriyās for a project/session.
 */
class SamjnaKriyaRegistry {

    private val registry = linkedMapOf<String, SamjnaKriya>()

    fun register(kriya: SamjnaKriya) {
        val existing = registry[kriya.nameStem]
        if (existing == null || kriya.isApavada || !existing.isApavada) {
            registry[kriya.nameStem] = kriya
        }
    }

    fun resolve(stem: String, callerSourceFile: String? = null): SamjnaKriya? {
        val kriya = registry[stem] ?: return null
        if (kriya.isInternal && callerSourceFile != null && kriya.sourceFile != null && callerSourceFile != kriya.sourceFile) {
            return null // File-private saṃjñā hidden from external caller
        }
        return kriya
    }

    fun all(): List<SamjnaKriya> = registry.values.toList()

    fun isEmpty(): Boolean = registry.isEmpty()

    val size: Int get() = registry.size

    fun detectInvocation(sentenceText: String, callerSourceFile: String? = null): SamjnaInvocation? {
        if (registry.isEmpty()) return null

        for ((_, kriya) in registry) {
            if (kriya.isInternal && callerSourceFile != null && kriya.sourceFile != null && callerSourceFile != kriya.sourceFile) {
                continue // File-private saṃjñā hidden from external caller
            }

            val segmentedStem = stripSupSuffix(kriya.nameSegmented)
            val instrumentalPattern = "$segmentedStem + टा"
            val patternIdx = sentenceText.indexOf(instrumentalPattern)
            if (patternIdx < 0) continue

            val afterInstrumental = sentenceText.substring(patternIdx + instrumentalPattern.length).trim()
            if (!afterInstrumental.startsWith("कृ")) continue

            val karmaText = sentenceText.substring(0, patternIdx).trim()

            return SamjnaInvocation(
                kriya = kriya,
                karmaText = karmaText,
                fullText = sentenceText,
            )
        }
        return null
    }

    companion object {
        private val SUP_SUFFIXES = setOf(
            "सुँ", "औ", "जस्", "अम्", "औट्", "शस्",
            "टा", "भ्याम्", "भिस्", "ङे", "भ्याम्", "भ्यस्",
            "ङसि", "भ्याम्", "भ्यस्", "ङस्", "ओस्", "आम्",
            "ङि", "ओस्", "सुप्",
        )

        internal fun stripSupSuffix(nameSegmented: String): String {
            val parts = nameSegmented.split("+").map { it.trim() }
            if (parts.size <= 1) return nameSegmented
            val lastPart = parts.last()
            return if (lastPart in SUP_SUFFIXES) {
                parts.dropLast(1).joinToString(" + ")
            } else {
                nameSegmented
            }
        }
    }
}

data class SamjnaInvocation(
    val kriya: SamjnaKriya,
    val karmaText: String,
    val fullText: String,
)
