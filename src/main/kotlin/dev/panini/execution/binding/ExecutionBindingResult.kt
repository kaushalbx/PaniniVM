package dev.panini.execution

sealed interface ExecutionBindingResult {
    data class Bound(val ukti: ExecutableUkti, val trace: List<String>) : ExecutionBindingResult
    data class NeedsInput(val message: String) : ExecutionBindingResult
    data class Invalid(val message: String) : ExecutionBindingResult
}
