package dev.panini.execution

/** Internal environment name carrying the typed result between ततः stages. */
internal const val PIPE_OPERAND: String = "विशेषणफल"

/** Typed operand injected into the कर्मन् position of a pipeline stage. */
data class InjectedKarmanBinding(
    val reference: String,
    val value: SanskritValue?,
)
