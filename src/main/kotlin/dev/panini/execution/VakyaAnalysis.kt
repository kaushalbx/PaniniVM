package dev.panini.execution

import dev.panini.derivation.Lakara

/** Output contract expected from a future Sanskrit sentence analyzer. */
data class VakyaAnalysis(
    val speaker: String,
    val listener: String,
    val sourceText: String,
    val prayojana: VakyaPrayojana,
    val polarity: Polarity = Polarity.POSITIVE,
    val lakara: Lakara? = null,
    val kriyas: List<KriyaAnalysis>,
    val dependencies: Set<ActionDependency> = kriyas.zipWithNext { before, after ->
        ActionDependency(before.id, after.id)
    }.toSet(),
)
