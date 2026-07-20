package dev.panini.parser

data class VakyaSyntaxDiagnostic(
    /**
     * ANTLR line numbers start at 1.
     */
    val line: Int,

    /**
     * ANTLR columns start at 0.
     */
    val column: Int,

    val message: String,

    val offendingText: String? = null,
) {
    override fun toString(): String =
        buildString {
            append("line ")
            append(line)
            append(':')
            append(column)
            append(' ')
            append(message)

            if (!offendingText.isNullOrBlank()) {
                append(" [")
                append(offendingText)
                append(']')
            }
        }
}
