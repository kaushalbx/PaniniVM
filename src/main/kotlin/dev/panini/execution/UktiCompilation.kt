package dev.panini.execution

sealed interface UktiCompilation {
    data class Compiled(val ukti: Ukti, val trace: List<String>) : UktiCompilation
    data class Invalid(val message: String) : UktiCompilation
}
