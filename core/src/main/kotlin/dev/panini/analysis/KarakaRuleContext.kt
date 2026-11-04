package dev.panini.analysis

import dev.panini.core.Karaka
import dev.panini.core.Prayoga
import dev.panini.dhatupatha.Dhatu
import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.KridantaPratipadika

data class KarakaRuleContext(
    val dhatu: DhatuIdentity,
    val participant: ParticipantFacts,
    val allParticipants: List<ParticipantFacts>,
    val prayoga: Prayoga,
    val candidates: Set<Karaka> = emptySet(),
    val verbNode: Pada? = null,
    val baseDhatu: Dhatu? = null,
    /** Explicit syntactic governor; presence elsewhere in the clause is insufficient. */
    val governingKrt: KridantaPratipadika? = null,
    val register: GrammarRegister = GrammarRegister.UNSPECIFIED,
    /** Selected reading of the linked kṛt: empty means action-only; null means unknown. */
    val governingKrtExpressedRoles: Set<dev.panini.core.Karaka>? = null,
    /** Occurrence identity distinguishes even two textually identical governors. */
    val governingKrtOccurrenceId: String? = null,
    val governingKrtGenitiveEligibility: KrtGenitiveEligibility? = null,
    /** Non-recursive participant resolutions prepared before testing written object mediation. */
    val mediatingObjectResolutions: Map<String, KarakaResolution> = emptyMap(),
)
