package dev.panini.execution

enum class InputValueType { TEXT, NUMBER, BOOLEAN, CHOICE }

data class InputRequest(
    val variableName: String,
    val type: InputValueType,
    val choices: List<String> = emptyList(),
    val minimum: Long? = null,
    val maximum: Long? = null,
) {
    init {
        require(type == InputValueType.CHOICE || choices.isEmpty()) { "Choices require the CHOICE input type." }
        require(type != InputValueType.CHOICE || choices.isNotEmpty()) { "CHOICE input requires at least one value." }
        require(type == InputValueType.NUMBER || minimum == null && maximum == null) {
            "Numeric bounds require the NUMBER input type."
        }
        require(minimum == null || maximum == null || minimum <= maximum) {
            "The minimum input bound cannot exceed the maximum."
        }
    }

    fun encode(): String = buildList {
        add(PREFIX)
        add(type.name)
        add(variableName)
        minimum?.let { add("$MINIMUM_PREFIX$it") }
        maximum?.let { add("$MAXIMUM_PREFIX$it") }
        addAll(choices)
    }.joinToString("\t")

    fun validate(rawValue: String): InputValidation = when (type) {
        InputValueType.TEXT -> InputValidation.Valid(rawValue)
        InputValueType.NUMBER -> validateNumber(rawValue)
        InputValueType.BOOLEAN -> rawValue.toInputBooleanOrNull()?.let {
            InputValidation.Valid(it.toString())
        } ?: InputValidation.Invalid("Invalid boolean '$rawValue'. Enter yes/no, true/false, आम्/न, or हाँ/नहीं.")
        InputValueType.CHOICE -> choices.firstOrNull { it.equals(rawValue.trim(), ignoreCase = true) }?.let {
            InputValidation.Valid(it)
        } ?: InputValidation.Invalid("Invalid choice '$rawValue'. Choose one of: ${choices.joinToString()}.")
    }

    companion object {
        private const val PREFIX = "PVM_INPUT"
        private const val MINIMUM_PREFIX = "MIN="
        private const val MAXIMUM_PREFIX = "MAX="

        fun decode(payload: String): InputRequest? {
            val fields = payload.split('\t')
            if (fields.size < 3 || fields[0] != PREFIX) return null
            val type = runCatching { InputValueType.valueOf(fields[1]) }.getOrNull() ?: return null
            val attributes = fields.drop(3)
            val minimum = attributes.firstOrNull { it.startsWith(MINIMUM_PREFIX) }
                ?.removePrefix(MINIMUM_PREFIX)?.toLongOrNull()
            val maximum = attributes.firstOrNull { it.startsWith(MAXIMUM_PREFIX) }
                ?.removePrefix(MAXIMUM_PREFIX)?.toLongOrNull()
            val choices = attributes.filterNot {
                it.startsWith(MINIMUM_PREFIX) || it.startsWith(MAXIMUM_PREFIX)
            }
            return runCatching { InputRequest(fields[2], type, choices, minimum, maximum) }.getOrNull()
        }
    }

    private fun validateNumber(rawValue: String): InputValidation {
        val number = rawValue.toInputLongOrNull()
            ?: return InputValidation.Invalid("Invalid number '$rawValue'. Enter ASCII or Devanagari digits.")
        if (minimum != null && number < minimum || maximum != null && number > maximum) {
            val expected = when {
                minimum != null && maximum != null -> "$minimum through $maximum"
                minimum != null -> "$minimum or greater"
                else -> "$maximum or less"
            }
            return InputValidation.Invalid("Number '$rawValue' is outside the allowed range. Enter $expected.")
        }
        return InputValidation.Valid(rawValue)
    }
}

sealed interface InputValidation {
    data class Valid(val value: String) : InputValidation
    data class Invalid(val message: String) : InputValidation
}

fun String.toInputLongOrNull(): Long? {
    val normalized = trim().map { character -> devanagariDigits[character] ?: character }.joinToString("")
    return normalized.toLongOrNull()
}

fun String.toInputBooleanOrNull(): Boolean? = when (trim().lowercase()) {
    "true", "yes", "y", "1", "आम्", "हाँ", "हां", "सत्य" -> true
    "false", "no", "n", "0", "न", "नहीं", "असत्य" -> false
    else -> null
}

private val devanagariDigits = mapOf(
    '०' to '0', '१' to '1', '२' to '2', '३' to '3', '४' to '4',
    '५' to '5', '६' to '6', '७' to '7', '८' to '8', '९' to '9',
)
