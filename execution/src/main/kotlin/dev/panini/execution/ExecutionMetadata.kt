package dev.panini.execution

/** Canonical keys exchanged between binding, execution, and conversation memory. */
internal object ExecutionMetadata {
    const val DEFAULT_DHATU = "dhatuName"

    fun dhatu(resultId: String): String = "dhatu:$resultId"
}
