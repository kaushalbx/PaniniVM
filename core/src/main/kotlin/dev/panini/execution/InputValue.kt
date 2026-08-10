package dev.panini.execution

enum class InputValueType { TEXT, NUMBER }

data class InputRequest(val variableName: String, val type: InputValueType) {
    fun encode(): String = "$PREFIX\t${type.name}\t$variableName"

    companion object {
        private const val PREFIX = "PVM_INPUT"

        fun decode(payload: String): InputRequest? {
            val fields = payload.split('\t', limit = 3)
            if (fields.size != 3 || fields[0] != PREFIX) return null
            val type = runCatching { InputValueType.valueOf(fields[1]) }.getOrNull() ?: return null
            return InputRequest(fields[2], type)
        }
    }
}

fun String.toInputLongOrNull(): Long? {
    val normalized = trim().map { character -> devanagariDigits[character] ?: character }.joinToString("")
    return normalized.toLongOrNull()
}

private val devanagariDigits = mapOf(
    '०' to '0', '१' to '1', '२' to '2', '३' to '3', '४' to '4',
    '५' to '5', '६' to '6', '७' to '7', '८' to '8', '९' to '9',
)
