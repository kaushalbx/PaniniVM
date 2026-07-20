package dev.panini.parser.ast

/**
 * Multiple expressions connected by च.
 *
 * Example:
 *
 *     एक + अम् द्वि + औट् त्रि + शस् च
 */
data class ParsedCoordination(
    val members: List<ParsedExpression>,
    val conjunction: String = "च",
) : ParsedExpression {

    init {
        require(members.size >= 2) {
            "A parsed coordination requires at least two members."
        }

        require(conjunction.isNotBlank()) {
            "A coordination requires a conjunction."
        }

        require(
            members.none {
                it is ParsedCoordination && it.members.isEmpty()
            },
        ) {
            "A coordination cannot contain an empty nested coordination."
        }
    }

    constructor(
        vararg members: ParsedExpression,
    ) : this(
        members = members.toList(),
    )

    override val segmentedText: String
        get() = buildString {
            append(
                members.joinToString(" ") {
                    it.segmentedText
                },
            )
            append(' ')
            append(conjunction)
        }
}
