package dev.panini.execution

/** Discourse turn coupling a system response with conversation context. */
data class SambhashanaTurn(
    val response: Prativacana,
    val context: SambhashanaContext,
)

/** A language-facing response to an interpreted utterance. */
data class Prativacana(
    val text: String,
    val phala: Phala,
)
