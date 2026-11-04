package dev.panini.analysis

import dev.panini.core.Karaka

/**
 * Contextually established eligibility under one kṛt occurrence, before 2.3.66.
 * Roles may include an implied argument; two written dependents are not required.
 * This is a semantic premise, not a deduction from the governor's suffix alone.
 */
data class KrtGenitiveEligibility(
    val governorOccurrenceId: String,
    val eligibleRoles: Set<Karaka>,
) {
    init {
        require(governorOccurrenceId.isNotBlank())
        require(eligibleRoles.all { it == Karaka.KARTR || it == Karaka.KARMAN })
    }
}
