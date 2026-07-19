package dev.panini.execution

/** A language-facing response to an interpreted utterance. */
data class Prativacana(
    val text: String,
    val phala: Phala,
)

