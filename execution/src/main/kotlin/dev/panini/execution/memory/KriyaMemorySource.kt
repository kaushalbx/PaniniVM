package dev.panini.execution.memory

/** Canonical source form used as the persisted kriyā analysis-cache key. */
internal object KriyaMemorySource {
    fun normalize(source: String): String = source.trim().let { trimmed ->
        if (trimmed.endsWith("।")) trimmed else "$trimmed ।"
    }
}
