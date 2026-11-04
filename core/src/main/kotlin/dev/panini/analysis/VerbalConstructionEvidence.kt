package dev.panini.analysis

import dev.panini.core.Karaka
import dev.panini.core.Upasarga
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.TingantaPada

/** Structural constructions require independent participant-role evidence. */
object VerbalConstructionEvidence {
    fun enrich(context: KarakaRuleContext): KarakaRuleContext {
        val verb = context.verbNode as? TingantaPada
        val governor = context.governingKrt
        val prefixes = governor?.upasargaIdentities ?: verb?.upasargaIdentities ?: return context
        val root = governor?.dhatu?.mulaDhatu ?: verb?.dhatu?.mulaDhatu ?: return context
        if (prefixes.any { it == null } || Upasarga.SAM !in prefixes ||
            root !in setOf("ज्ञा", "ज्ञा॒")) return context
        val original = context.participant
        if (SemanticRelation.DESIRED_OBJECT !in original.semanticRelations ||
            original.possibleVibhaktis.none { it in setOf(Vibhakti.DVITIYA, Vibhakti.TRTIYA) }) return context
        val premise = SemanticRelationEvidence(
            SemanticRelation.SAM_JNA_OBJECT, SemanticEvidenceSource.GOVERNING_CONSTRUCTION,
            "Explicit sam-jñā AST governs an independently established object hypothesis (2.3.22).",
        )
        val participant = original.copy(
            semanticRelations = original.semanticRelations + premise.relation,
            semanticEvidence = original.semanticEvidence + premise,
        )
        return context.copy(
            participant = participant,
            allParticipants = context.allParticipants.map { if (it.id == participant.id) participant else it },
            candidates = context.candidates.ifEmpty {
                KarakaInference.candidatesForCases(original.possibleVibhaktis, context.prayoga, context.dhatu.sakarmaka)
            } + Karaka.KARMAN,
        )
    }
}
