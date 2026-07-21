package dev.panini.vyakaranam.parser

data class PaniniSyntaxError(
    val line: Int,
    val column: Int,
    val offendingText: String?,
    val message: String,
)

class PaniniParseException(
    val errors: List<PaniniSyntaxError>,
) : IllegalArgumentException(
    errors.joinToString(
        prefix = "संस्कृतव्याकरणस्य विश्लेषणे दोषाः:\n",
        separator = "\n",
    ) { error ->
        buildString {
            append("पङ्क्तिः ")
            append(error.line)
            append(", स्थानम् ")
            append(error.column)

            error.offendingText?.let {
                append(", पदम्='")
                append(it)
                append('\'')
            }

            append(": ")
            append(error.message)
        }
    },
)
