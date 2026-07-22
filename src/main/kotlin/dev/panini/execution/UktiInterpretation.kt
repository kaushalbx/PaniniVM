package dev.panini.execution

sealed interface UktiInterpretation {
    data class Understood(val ukti: Ukti, val trace: List<String>) : UktiInterpretation
    data class NeedsClarification(val question: String) : UktiInterpretation
    data class Contradictory(val reason: String) : UktiInterpretation
}
