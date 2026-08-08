package dev.panini.execution

/** Canonical keys exchanged between binding, execution, and conversation memory. */
internal object ExecutionMetadata {
    const val DEFAULT_DHATU = "dhatuName"
    const val FREQUENCY_COUNT = "frequencyCount"

    fun dhatu(resultId: String): String = "dhatu:$resultId"
}
