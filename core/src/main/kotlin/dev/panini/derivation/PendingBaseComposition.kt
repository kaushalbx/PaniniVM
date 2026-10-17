package dev.panini.derivation

import dev.panini.shiksha.Varna

/** A substitution-exposed junction that must compose before the base is used. */
data class PendingBaseComposition(
    val index: Int,
    val originalPair: List<Varna>,
    val introducedBySutra: String,
) {
    init {
        require(index >= 0 && originalPair.size == 2 && introducedBySutra.isNotBlank())
    }
}
