package dev.panini.execution.persistence

internal object StateFileSchema {
    const val HEADER_V2 = "PANINI_STATE_V2"
}

internal enum class StateRecordType {
    CONTEXT,
    ENTITY,
    RESULT,
    HISTORY,
    META;

    companion object {
        fun fromWireName(value: String?): StateRecordType? = entries.firstOrNull { it.name == value }
    }
}
