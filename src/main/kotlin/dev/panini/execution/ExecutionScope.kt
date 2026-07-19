package dev.panini.execution

/** Capabilities come from the host, never from claims inside the utterance. */
data class ExecutionScope(
    val capabilities: Set<ExecutionEffect> = setOf(ExecutionEffect.PURE),
    val variables: Map<String, String> = emptyMap(),
    val variableSamjnas: Map<String, Set<ExecutionSamjna>> = emptyMap(),
    /** Verified identities whose commands the listener is configured to obey. */
    val authorizedSpeakers: Set<String> = emptySet(),
    /** Requests accepted by the listener; acceptance is not inferred from the sentence. */
    val acceptedInvocations: Set<String> = emptySet(),
)
