package dev.panini.analysis

import dev.panini.core.Karaka
import dev.panini.core.Vibhakti

data class VibhaktiRuleContext(
    val karaka: Karaka,
    val morphologicalCandidates: Set<Vibhakti>,
    val abhihita: Boolean = false,
    val participant: ParticipantFacts? = null,
) {
    fun accepts(expectedKaraka: Karaka, vibhakti: Vibhakti): Boolean =
        !abhihita && karaka == expectedKaraka && vibhakti in morphologicalCandidates
}
