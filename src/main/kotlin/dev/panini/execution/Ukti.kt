package dev.panini.execution

import dev.panini.derivation.Lakara

data class Ukti(
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
) {
    init {
        require(text.isNotBlank()) { "An utterance requires text." }
        require(invocations.isNotEmpty()) { "An executable utterance requires at least one dhātu invocation." }
    }
}
