package dev.panini.analysis

import dev.panini.core.Karaka

/** Reading of one explicitly linked kṛt nominal occurrence; null means unknown. */
data class KrtGovernorReading(
    val governorIndex: Int,
    val expressedRoles: Set<Karaka>?,
    val genitiveEligibleRoles: Set<Karaka>? = null,
) {
    init {
        require(governorIndex >= 0)
        require(genitiveEligibleRoles.orEmpty().all { it == Karaka.KARTR || it == Karaka.KARMAN })
    }
}
