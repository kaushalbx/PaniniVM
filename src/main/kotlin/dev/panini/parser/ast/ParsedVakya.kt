package dev.panini.parser.ast

data class ParsedVakya(
    /**
     * All nominal, coordinated, kṛdanta and indeclinable expressions
     * associated with this verb.
     *
     * Their original source order is retained.
     */
    val padas: List<ParsedPada>,

    /**
     * The main verb of the sentence. Optional in Sanskrit (e.g., nominal sentences).
     */
    val tinganta: ParsedTinganta? = null,
)
