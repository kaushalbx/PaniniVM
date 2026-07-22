package dev.panini.execution

import dev.panini.core.Karaka
import dev.panini.dhatupatha.Dhatu
import dev.panini.execution.persistence.StateStore
import dev.panini.execution.external.ExternalCapabilityDispatcher

/** One occurrence of a verb in an utterance. */
data class DhatuInvocation(
    val id: String,
    val dhatu: Dhatu,
    val bindings: Map<Karaka, ExecutionExpression>,
    val selectedOperation: String? = null,
    val metadata: Map<String, String> = emptyMap(),
    val grammaticalFeatures: GrammaticalFeatures = GrammaticalFeatures(),
) {
    init {
        require(id.isNotBlank()) { "A dhātu invocation requires an id." }
    }

    fun executionContext(
        variables: Map<String, SanskritValue>,
        stateStore: StateStore? = null,
        externalDispatcher: ExternalCapabilityDispatcher? = null,
    ): ExecutionContext = ExecutionContext(
        bindings = bindings,
        selectedOperation = selectedOperation,
        variables = variables,
        metadata = metadata,
        stateStore = stateStore,
        externalDispatcher = externalDispatcher,
    )

    fun executionContext(
        variables: Map<String, String>,
        variableSamjnas: Map<String, Set<ExecutionSamjna>> = emptyMap(),
        stateStore: StateStore? = null,
        externalDispatcher: ExternalCapabilityDispatcher? = null,
    ): ExecutionContext = ExecutionContext(
        bindings = bindings,
        selectedOperation = selectedOperation,
        rawVariables = variables,
        variableSamjnas = variableSamjnas,
        metadata = metadata,
        stateStore = stateStore,
        externalDispatcher = externalDispatcher,
    )
}
