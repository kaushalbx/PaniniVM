package dev.panini.execution

import dev.panini.shiksha.Samjna

sealed interface Phala {
    data class Siddha(
        val values: Map<String, String>,
        val samjnas: Map<String, Set<Samjna>>,
        val trace: List<String>,
        val typedValues: Map<String, SanskritValue> = emptyMap(),
        val localBindings: Map<String, SanskritValue> = emptyMap(),
        val metadata: Map<String, String> = emptyMap(),
    ) : Phala
    data class Asiddha(val result: ExecutionResult, val trace: List<String>) : Phala
    data class AnumatiApekshita(
        val invocationId: String,
        val effects: Set<ExecutionEffect>,
        val continuation: ExecutionContinuation,
    ) : Phala
    data class SvikaraApekshita(
        val invocationId: String,
        val speaker: String,
        val listener: String,
        val continuation: ExecutionContinuation,
    ) : Phala
    data class Nirasta(val invocationId: String, val reason: String) : Phala
    /** The utterance was understood and planned, but its purpose does not request performance. */
    data class Avagata(
        val disposition: ExecutionDisposition,
        val plans: List<ExecutionPlan>,
        val trace: List<String>,
    ) : Phala
}
