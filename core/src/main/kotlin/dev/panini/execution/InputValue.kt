package dev.panini.execution

enum class InputValueType { TEXT, NUMBER, BOOLEAN, CHOICE }

data class InputRequest(
    val variableName: String,
    val type: InputValueType,
    val choices: List<String> = emptyList(),
) {
    init {
        require(type == InputValueType.CHOICE || choices.isEmpty()) { "Choices require the CHOICE input type." }
        require(type != InputValueType.CHOICE || choices.isNotEmpty()) { "CHOICE input requires at least one value." }
    }

    fun encode(): String = listOf(PREFIX, type.name, variableName, *choices.toTypedArray()).joinToString("\t")

    fun validate(rawValue: String): InputValidation = when (type) {
        InputValueType.TEXT -> InputValidation.Valid(rawValue)
        InputValueType.NUMBER -> if (rawValue.toInputLongOrNull() != null) {
            InputValidation.Valid(rawValue)
        } else {
            InputValidation.Invalid("Invalid number '$rawValue'. Enter ASCII or Devanagari digits.")
        }
        InputValueType.BOOLEAN -> rawValue.toInputBooleanOrNull()?.let {
            InputValidation.Valid(it.toString())
        } ?: InputValidation.Invalid("Invalid boolean '$rawValue'. Enter yes/no, true/false, आम्/न, or हाँ/नहीं.")
        InputValueType.CHOICE -> choices.firstOrNull { it.equals(rawValue.trim(), ignoreCase = true) }?.let {
            InputValidation.Valid(it)
        } ?: InputValidation.Invalid("Invalid choice '$rawValue'. Choose one of: ${choices.joinToString()}.")
    }

    companion object {
        private const val PREFIX = "PVM_INPUT"

        fun decode(payload: String): InputRequest? {
            val fields = payload.split('\t')
            if (fields.size < 3 || fields[0] != PREFIX) return null
            val type = runCatching { InputValueType.valueOf(fields[1]) }.getOrNull() ?: return null
            return runCatching { InputRequest(fields[2], type, fields.drop(3)) }.getOrNull()
        }
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
