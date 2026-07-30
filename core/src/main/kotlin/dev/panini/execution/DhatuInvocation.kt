package dev.panini.execution

import dev.panini.core.Karaka
import dev.panini.dhatupatha.Dhatu

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
