package dev.panini.analysis

import dev.panini.core.KrtAffix
import dev.panini.core.Karaka
import dev.panini.vyakaranam.ast.isRecognizedStriAffix
import dev.panini.vyakaranam.ast.KrtDerivationSense

/** Derives structural premises from an explicitly linked kṛt governor. */
object KrtGovernanceEvidence {
    fun hasRecognizedMorphology(governor: dev.panini.vyakaranam.ast.KridantaPratipadika): Boolean =
        governor.krtAffix != null &&
            governor.upasargas.all { dev.panini.core.Upasarga.fromUpadesha(it) != null } &&
            governor.dhatu.sanadiAffixes.all { it != null }

    private val krtya = setOf(KrtAffix.TAVYAT, KrtAffix.ANIYAR, KrtAffix.YAT, KrtAffix.NYAT)
    private val prohibited = setOf(
        KrtAffix.KTA, KrtAffix.KTAVATU,
        KrtAffix.KTVA, KrtAffix.LYAP, KrtAffix.TUMUN, KrtAffix.NAMUL,
        KrtAffix.SHATR, KrtAffix.SHANAC, KrtAffix.KANAC, KrtAffix.KVASU,
        KrtAffix.KI, KrtAffix.KIN, KrtAffix.U, KrtAffix.UKAN,
        KrtAffix.KHAL, KrtAffix.YUC, KrtAffix.SHANAN, KrtAffix.CANASH, KrtAffix.TRN,
    )

    fun enrich(context: KarakaRuleContext): KarakaRuleContext {
        val governor = context.governingKrt ?: return context
        if (!hasRecognizedMorphology(governor)) return context.copy(governingKrtExpressedRoles = null)
        val affix = governor.krtAffix ?: return context
        // A taddhita built on a kṛt stem is not direct kṛt governance (kṛtapūrvī kaṭam).
        if (governor.vikaras.any { !it.isRecognizedStriAffix }) return context
        val derivation = governor.derivationEvidence
        val prohibition70 = when {
            affix == KrtAffix.NVUL && derivation?.governingSutra == "3.3.10" &&
                derivation.sense == KrtDerivationSense.FUTURE -> KrtGenitiveProhibition.AKA_FUTURE_DERIVATION
            affix in setOf(KrtAffix.INI, KrtAffix.NINI) && derivation?.governingSutra == "3.3.3" &&
                derivation.sense == KrtDerivationSense.FUTURE -> KrtGenitiveProhibition.IN_FUTURE
            affix == KrtAffix.NINI && derivation?.governingSutra == "3.3.170" &&
                derivation.sense == KrtDerivationSense.DEBT -> KrtGenitiveProhibition.IN_DEBT
            else -> null
        }
        val unverifiedProhibitionClaim =
            (context.participant.isKrtProhibitedForSasthi && affix !in prohibited) ||
            (context.participant.krtGenitiveProhibition != null && context.participant.krtGenitiveProhibition != prohibition70)
        val dualMarkers = setOf(SemanticRelation.KRT_DUAL_ELIGIBLE_AGENT, SemanticRelation.KRT_DUAL_ELIGIBLE_OBJECT)
        val independentRoles = buildSet {
            if (context.participant.semanticRelations.any {
                    it in setOf(SemanticRelation.INDEPENDENT_AGENT, SemanticRelation.PROMPTER_CAUSE)
                }) add(Karaka.KARTR)
            if (SemanticRelation.DESIRED_OBJECT in context.participant.semanticRelations) add(Karaka.KARMAN)
        }
        val eligibility = context.governingKrtGenitiveEligibility
        val eligibilityValid = eligibility != null &&
            eligibility.governorOccurrenceId == context.governingKrtOccurrenceId &&
            context.governingKrtExpressedRoles != null &&
            eligibility.eligibleRoles.intersect(context.governingKrtExpressedRoles).isEmpty() &&
            eligibility.eligibleRoles.containsAll(independentRoles - context.governingKrtExpressedRoles)
        // Supplied markers cannot bind themselves to whichever kṛt happens to
        // govern this dependent. Reject an unbound/contradictory supplied claim.
        val invalidClaim = (eligibility != null && !eligibilityValid) ||
            (context.participant.semanticRelations.any { it in dualMarkers } && !eligibilityValid)
        val bothEligible = eligibilityValid && eligibility!!.eligibleRoles.containsAll(setOf(Karaka.KARTR, Karaka.KARMAN))
        val relations = buildSet {
            add(SemanticRelation.KRT_GOVERNED_PARTICIPANT)
            if (affix == KrtAffix.KTA && Karaka.ADHIKARANA in context.governingKrtExpressedRoles.orEmpty()) {
                add(SemanticRelation.LOCATION_PARTICIPLE_RELATION)
            }
            if (bothEligible) {
                // These describe eligibility under this governor, not a new
                // saṃjñā. The case rules still require the independently
                // established KARTR/KARMAN result, including special karmans.
                add(SemanticRelation.KRT_DUAL_ELIGIBLE_AGENT)
                add(SemanticRelation.KRT_DUAL_ELIGIBLE_OBJECT)
            }
            if (affix in krtya && SemanticRelation.INDEPENDENT_AGENT in context.participant.semanticRelations) {
                add(SemanticRelation.KRTYA_GOVERNED_AGENT)
            }
        }
        val participant = context.participant.copy(
            semanticRelations = context.participant.semanticRelations - dualMarkers + relations,
            semanticEvidence = context.participant.semanticEvidence.filterNotTo(mutableSetOf()) { it.relation in dualMarkers } + relations.map {
                SemanticRelationEvidence(
                    it, SemanticEvidenceSource.GOVERNING_CONSTRUCTION,
                    "Explicit kṛt governor ${governor.sourceText} with affix ${affix.upadesha}; " +
                        "expressed roles: ${context.governingKrtExpressedRoles ?: "unknown reading"}; " +
                        "derivation: ${derivation ?: "unspecified"}; " +
                        "governor occurrence: ${context.governingKrtOccurrenceId ?: "unknown"}; " +
                        "bound genitive eligibility: ${eligibility.takeIf { eligibilityValid } ?: "unverified"}.",
                )
            },
            isKrtProhibitedForSasthi = context.participant.isKrtProhibitedForSasthi || affix in prohibited,
            krtGenitiveProhibition = prohibition70 ?: context.participant.krtGenitiveProhibition,
        )
        val candidates = context.candidates.ifEmpty {
            KarakaInference.candidatesForCases(participant.possibleVibhaktis, context.prayoga, context.dhatu.sakarmaka)
        }
        return context.copy(
            participant = participant,
            allParticipants = context.allParticipants.map { if (it.id == participant.id) participant else it },
            candidates = candidates + independentRoles.takeIf { it.size == 1 }.orEmpty() +
                if (SemanticRelation.CAUSEE_AGENT in participant.semanticRelations)
                    setOf(Karaka.KARMAN, Karaka.KARTR) else emptySet(),
            governingKrtExpressedRoles = context.governingKrtExpressedRoles.takeUnless { invalidClaim || unverifiedProhibitionClaim },
        )
    }
}
