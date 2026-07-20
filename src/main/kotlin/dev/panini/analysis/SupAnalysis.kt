package dev.panini.analysis

import dev.panini.derivation.Vacana
import dev.panini.derivation.Vibhakti

data class SupPosition(
    val vibhakti: Vibhakti,
    val vacana: Vacana,
)

data class SupAnalysis(
    val pratyaya: String,
    val candidates: Set<SupPosition>,
) {
    init {
        require(pratyaya.isNotBlank()) {
            "A sup analysis requires a pratyaya."
        }

        require(candidates.isNotEmpty()) {
            "A sup analysis requires at least one candidate."
        }
    }

    val isAmbiguous: Boolean
        get() = candidates.size > 1

    val resolvedPosition: SupPosition?
        get() = candidates.singleOrNull()
}
