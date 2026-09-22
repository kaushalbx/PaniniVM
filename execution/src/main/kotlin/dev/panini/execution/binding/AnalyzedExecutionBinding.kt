package dev.panini.execution.binding

import dev.panini.analysis.UktiAnalysis
import dev.panini.execution.ExecutionBindingResult

/** Binding result together with the grammatical analysis produced by the same pass. */
internal data class AnalyzedExecutionBinding(
    val binding: ExecutionBindingResult,
    val analysis: UktiAnalysis? = null,
)
