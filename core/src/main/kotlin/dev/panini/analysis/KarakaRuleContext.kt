package dev.panini.analysis

import dev.panini.core.Karaka
import dev.panini.core.Prayoga
import dev.panini.dhatupatha.Dhatu
import dev.panini.vyakaranam.ast.Pada

data class KarakaRuleContext(
    val dhatu: DhatuIdentity,
    val participant: ParticipantFacts,
    val allParticipants: List<ParticipantFacts>,
    val prayoga: Prayoga,
    val candidates: Set<Karaka> = emptySet(),
    val verbNode: Pada? = null,
    val baseDhatu: Dhatu? = null,
)
