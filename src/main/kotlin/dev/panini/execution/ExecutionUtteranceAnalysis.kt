package dev.panini.execution

import dev.panini.core.Lakara

/** Execution-facing semantic description produced from grammatical analysis. */
data class ExecutionUtteranceAnalysis(
    val speaker: String,
    val listener: String,
    val sourceText: String,
    val prayojana: VakyaPrayojana,
    val polarity: Polarity = Polarity.POSITIVE,
    val lakara: Lakara? = null,
    val kriyas: List<ExecutionKriyaAnalysis>,
    val dependencies: Set<ActionDependency> = kriyas.zipWithNext { before, after ->
        ActionDependency(before.id, after.id)
    }.toSet(),
)
