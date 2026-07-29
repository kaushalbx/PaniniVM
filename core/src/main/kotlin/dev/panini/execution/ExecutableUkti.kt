package dev.panini.execution

import dev.panini.core.Lakara

data class ExecutableUkti(
    val speaker: String,
    val listener: String,
    val text: String,
    val prayojana: VakyaPrayojana,
    val polarity: Polarity = Polarity.POSITIVE,
    val lakara: Lakara? = null,
    val invocations: List<DhatuInvocation>,
    val dependencies: Set<ActionDependency> = invocations.zipWithNext { before, after ->
        ActionDependency(before.id, after.id)
    }.toSet(),
    val controlRelations: Set<ExecutionControlRelation> = emptySet(),
) {
    init {
        require(text.isNotBlank()) { "An utterance requires text." }
        require(invocations.isNotEmpty()) { "An executable utterance requires at least one dhātu invocation." }
        val invocationIds = invocations.mapTo(mutableSetOf()) { it.id }
        controlRelations.forEach { relation ->
            require(relation.condition in invocationIds && relation.body in invocationIds) {
                "A control relation must refer to invocations in the same utterance."
            }
        }
    }
}

sealed interface ExecutionControlRelation {
    val condition: String
    val body: String

    data class ConditionalDuration(
        override val condition: String,
        override val body: String,
        val maximumIterations: Int = 1_000,
    ) : ExecutionControlRelation {
        init {
            require(maximumIterations > 0) { "A repetition limit must be positive." }
        }
    }
}
