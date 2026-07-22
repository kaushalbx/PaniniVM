package dev.panini.execution

sealed interface ExecutionCompilation {
    data class Compiled(val ukti: Ukti, val trace: List<String>) : ExecutionCompilation
    data class Invalid(val message: String) : ExecutionCompilation
}

