package dev.panini.execution

import dev.panini.dhatupatha.Dhatu

/** One occurrence of a verb in an utterance. */
data class DhatuInvocation(
    val id: String,
    val dhatu: Dhatu,
    val bindings: Map<Karaka, ExecutionExpression>,
    val selectedOperation: String? = null,
    val metadata: Map<String, String> = emptyMap(),
) {
    init {
        require(id.isNotBlank()) { "A dhātu invocation requires an id." }
    }

    fun executionContext(
        variables: Map<String, String>,
        variableSamjnas: Map<String, Set<ExecutionSamjna>> = emptyMap(),
    ): ExecutionContext = ExecutionContext(
        bindings = bindings,
        selectedOperation = selectedOperation,
        variables = variables,
        variableSamjnas = variableSamjnas,
        metadata = metadata,
    )
}
