package dev.panini.derivation

/** Doubling (8.4.46, 8.4.47) and pause nasalisation (8.4.57) are options at almost every cluster
 * and pause, independent of the rule a test examines; such a test sets them aside. They are
 * tested on their own in TukAndDvitvaSandhiTest. */
internal val independentOptions = setOf("8.4.46", "8.4.47", "8.4.57")

internal fun List<DerivationResult>.withoutIndependentOptions(): List<DerivationResult> =
    filter { result -> result.applications.none { it.sutra in independentOptions } }
