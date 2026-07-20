package dev.panini.parser.ast

/**
 * Complete parser-level representation of an utterance.
 *
 * Example:
 *
 *     हे यन्त्र + सुँ,
 *     एक + अम् द्वि + औट् त्रि + शस् च
 *     युज् + णिच् + लोट् + सिप् ।
 */
data class ParsedUtterance(
    val sambodhana: ParsedSambodhana? = null,
    val statements: List<ParsedVakya>,
    val connectives: List<String> = emptyList(),
    val hasDanda: Boolean = false,
) {

    init {
        require(statements.isNotEmpty()) {
            "A parsed utterance requires at least one statement."
        }

        require(
            connectives.none(String::isBlank),
        ) {
            "Connectives cannot contain blank values."
        }

        require(connectives.size == statements.size - 1) {
            "There must be exactly one connective between adjacent statements."
        }
    }
}
