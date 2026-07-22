package dev.panini.execution

import dev.panini.core.Lakara

data class Nirdesha(
    val speaker: String,
    val listener: String,
    val prayojana: VakyaPrayojana,
    val polarity: Polarity,
    val lakara: Lakara?,
    val invocations: List<DhatuInvocation>,
    val sourceText: String,
)
