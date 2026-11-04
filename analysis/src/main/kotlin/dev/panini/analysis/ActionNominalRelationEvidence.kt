package dev.panini.analysis

import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.KridantaPratipadika
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.MulaPratipadikaIdentity
import dev.panini.vyakaranam.ast.SubantaPada

/** Written action-result/participant relation, independent of runtime action history. */
internal object ActionNominalRelationEvidence {
    fun infer(padas: List<SubantaAnalysis>, context: VakyaSemanticContext): Map<Int, SemanticRelationEvidence> {
        val result = padas.indices.singleOrNull { index ->
            val stem = (padas[index].pada as? SubantaPada)?.pratipadika as? MulaPratipadika
            stem != null && stem.vikaras.isEmpty() &&
                (stem.lexicalIdentity == MulaPratipadikaIdentity.PHALA ||
                    dev.panini.core.Karaka.fromPratipadika(stem.text) == dev.panini.core.Karaka.KARMAN)
        } ?: return emptyMap()
        val modifier = padas.indices.filter { index ->
            padas[index].supCandidates.any { it.vibhakti == Vibhakti.SASTHI }
        }.singleOrNull() ?: return emptyMap()
        if (modifier == result || padas[modifier].supCandidates.map { it.vibhakti }.toSet() != setOf(Vibhakti.SASTHI) ||
            (padas[modifier].pada as? SubantaPada)?.pratipadika !is KridantaPratipadika ||
            modifier in context.krtGovernors || modifier in context.avyayaGovernors || modifier in context.purposeGovernors ||
            context.participantRelations[modifier].orEmpty().isNotEmpty()) return emptyMap()
        return mapOf(modifier to SemanticRelationEvidence(SemanticRelation.SESHA_RELATION,
            SemanticEvidenceSource.GOVERNING_CONSTRUCTION,
            "Written genitive action nominal p_$modifier identifies the action whose result or object is denoted by p_$result."))
    }
}
