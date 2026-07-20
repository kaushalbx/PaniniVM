package dev.panini.parser.ast

/**
 * A reference to a previously calculated or stored value.
 *
 * Examples:
 *
 *     फल
 *     फल + ङि
 *     पूर्वफल + अम्
 */
data class ParsedReference(
    val name: String,
    val pratyaya: String? = null,
) : ParsedExpression {

    init {
        require(name.isNotBlank()) {
            "A parsed reference requires a non-blank name."
        }

        require(pratyaya?.isNotBlank() != false) {
            "A reference pratyaya cannot be blank when provided."
        }
    }

    override val segmentedText: String
        get() = pratyaya?.let {
            "$name + $it"
        } ?: name
}
