package dev.panini.analysis

data class KarakaEvidence(
    val sutra: String,
    val text: String,
    val reason: String,
    /** True when the matching rule licenses this result only optionally. */
    val optional: Boolean = false,
    /** False records the explicitly declined branch of an optional rule. */
    val applied: Boolean = true,
    /** Participant-level premises consumed while selecting this sūtra. */
    val semanticEvidence: Set<SemanticRelationEvidence> = emptySet(),
)
