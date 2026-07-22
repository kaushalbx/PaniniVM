package dev.panini.execution

/** A stable, turn-qualified result retained in discourse memory. */
data class SmrtaPhala(
    val id: String,
    val turnNumber: Int,
    val invocationId: String,
    val value: String,
    val samjnas: Set<ExecutionSamjna>,
    val typedValue: SanskritValue? = null,
)
