package dev.panini.parser.ast

data class ParsedVakya(
    /**
     * All nominal, coordinated, kṛdanta and indeclinable expressions
     * associated with this verb.
     *
     * Their original source order is retained.
     */
    val padas: List<ParsedPada>,

    val tinganta: ParsedTinganta,
)
