package dev.panini.parser

class VakyaSyntaxException(
    val diagnostics: List<VakyaSyntaxDiagnostic>,
    val sourceName: String? = null,
) : IllegalArgumentException(
    buildMessage(
        diagnostics = diagnostics,
        sourceName = sourceName,
    ),
) {
    init {
        require(diagnostics.isNotEmpty()) {
            "VakyaSyntaxException requires at least one diagnostic."
        }
    }

    companion object {

        private fun buildMessage(
            diagnostics: List<VakyaSyntaxDiagnostic>,
            sourceName: String?,
        ): String =
            buildString {
                append("Unable to parse")

                if (!sourceName.isNullOrBlank()) {
                    append(" ")
                    append(sourceName)
                }

                append(':')

                diagnostics.forEach { diagnostic ->
                    appendLine()
                    append(" - ")
                    append(diagnostic)
                }
            }
    }
}
