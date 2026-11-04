package dev.panini.analysis

import dev.panini.core.Karaka
import dev.panini.core.Vibhakti

data class VibhaktiRuleContext(
    val karaka: Karaka,
    val morphologicalCandidates: Set<Vibhakti>,
    val abhihita: Boolean = false,
    val participant: ParticipantFacts? = null,
    val dhatu: DhatuIdentity? = null,
    val verbNode: dev.panini.vyakaranam.ast.Pada? = null,
    val allParticipants: List<ParticipantFacts> = emptyList(),
    val baseDhatu: dev.panini.dhatupatha.Dhatu? = null,
    val register: GrammarRegister = GrammarRegister.UNSPECIFIED,
    val governingKrt: dev.panini.vyakaranam.ast.KridantaPratipadika? = null,
    val governingKrtExpressedRoles: Set<Karaka>? = null,
) {
    fun accepts(expectedKaraka: Karaka, vibhakti: Vibhakti): Boolean =
        !abhihita && karaka == expectedKaraka && vibhakti in morphologicalCandidates
}
