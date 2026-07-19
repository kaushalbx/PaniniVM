package dev.panini.execution

/** Trusted conversational situation surrounding an utterance. */
data class SambhashanaContext(
    val speaker: String,
    val listener: String,
    val mentionedEntities: Map<String, String> = emptyMap(),
    val mentionedEntitySamjnas: Map<String, Set<ExecutionSamjna>> = emptyMap(),
    val previousResults: Map<String, String> = emptyMap(),
    val previousResultSamjnas: Map<String, Set<ExecutionSamjna>> = emptyMap(),
    val resultHistory: List<SmrtaPhala> = emptyList(),
    val turnNumber: Int = 0,
    val metadata: Map<String, String> = emptyMap(),
) {
    init {
        require(speaker.isNotBlank()) { "A conversation requires a speaker." }
        require(listener.isNotBlank()) { "A conversation requires a listener." }
    }
}
