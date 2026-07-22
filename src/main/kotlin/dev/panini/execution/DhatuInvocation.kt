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
    val ambiguousBindings: List<AmbiguousKarakaBinding> = emptyList(),
    val karakaTrace: List<String> = emptyList(),
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

}

/** A syncretic nominal whose surface ending licenses more than one kāraka. */
data class AmbiguousKarakaBinding(
    val expression: ExecutionExpression,
    val candidates: Set<Karaka>,
) {
    init {
        require(candidates.size > 1) { "An ambiguous binding requires multiple kāraka candidates." }
    }
}
