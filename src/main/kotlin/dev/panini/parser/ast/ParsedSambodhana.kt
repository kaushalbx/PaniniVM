package dev.panini.parser.ast

/**
 * The addressed target at the beginning of an utterance.
 *
 * Example:
 *
 *     हे यन्त्र + सुँ
 */
data class ParsedSambodhana(
    val particle: String,
    val pada: ParsedSubanta,
)
