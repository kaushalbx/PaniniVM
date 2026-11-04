package dev.panini.analysis

import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.*

/** A coordinated genitive enumeration attached to one written collection head. */
internal object CollectionMembershipEvidence {
    fun infer(padas: List<SubantaAnalysis>, written: List<Pada>, head: TingantaPada,
        context: VakyaSemanticContext): Map<Int, SemanticRelationEvidence> {
        if (head.dhatu.mulaDhatu !in setOf("अस्", "असँ") || head.upasargas.isNotEmpty() ||
            head.dhatu.sanadiPratyayas.isNotEmpty()) return emptyMap()
        val collection = padas.indices.singleOrNull { index ->
            val stem = (padas[index].pada as? SubantaPada)?.pratipadika as? MulaPratipadika
            stem?.lexicalIdentity == MulaPratipadikaIdentity.SUCHI && stem.vikaras.isEmpty() &&
                padas[index].supCandidates.map { it.vibhakti }.toSet() == setOf(Vibhakti.PRATHAMA)
        } ?: return emptyMap()
        val members = padas.indices.filter { it != collection }
        if (members.size < 2 || members.any { index ->
                padas[index].supCandidates.none { it.vibhakti == Vibhakti.SASTHI } ||
                    index in context.krtGovernors || index in context.avyayaGovernors ||
                    index in context.purposeGovernors || context.participantRelations[index].orEmpty().isNotEmpty()
            } || members.none { index ->
                padas[index].supCandidates.map { it.vibhakti }.toSet() == setOf(Vibhakti.SASTHI)
            }) return emptyMap()
        // A final ca coordinates the members. Neither a nearby collection noun
        // nor an isolated syncretic ending is sufficient to establish the link.
        // Structured coordination already proves its connective; expose its
        // original member occurrences for the same validation as a flat ca group.
        val enumeration = written.flatMap { pada ->
            if (pada is SamuccitaSubanta) pada.members + AvyayaPada("च", "च") else listOf(pada)
        }.filter { it !== head && it !== padas[collection].pada }
        if ((enumeration.lastOrNull() as? AvyayaPada)?.form != "च") return emptyMap()
        val nominals = enumeration.filterNot { it is AvyayaPada }
        if (nominals.size != members.size || nominals.any { pada -> members.none { padas[it].pada === pada } } ||
            enumeration.withIndex().any { (index, pada) -> pada is AvyayaPada &&
                (pada.form != "च" || index == 0 || enumeration[index - 1] is AvyayaPada) }) return emptyMap()
        return members.associateWith { index -> SemanticRelationEvidence(
            SemanticRelation.SESHA_RELATION, SemanticEvidenceSource.GOVERNING_CONSTRUCTION,
            "Coordinated genitive enumeration p_${members.joinToString(",p_")} supplies members of written collection p_$collection; p_$index shares that nominal relation.") }
    }
}
