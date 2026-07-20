package dev.panini.parser.ast

/**
 * Base type for expressions occurring as arguments around a tiṅanta.
 *
 * This is parser-level structure only. It does not assign kārakas or
 * executable operations.
 */
sealed interface ParsedExpression {

    /**
     * Source representation reconstructed from the parsed components.
     */
    val segmentedText: String
}
