package dev.panini.core

/** Lexical action structure, independent of tense, person, and discourse occurrence. */
data class ActionIdentity(
    val dhatupathaId: String,
    val upasargas: List<Upasarga> = emptyList(),
    val sanadi: List<SanadiAffix> = emptyList(),
) {
    init { require(dhatupathaId.isNotBlank()) { "An action requires a resolved Dhātupāṭha identity." } }
}
