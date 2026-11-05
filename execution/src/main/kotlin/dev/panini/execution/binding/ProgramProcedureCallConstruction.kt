package dev.panini.execution.binding

import dev.panini.analysis.FrameKarakaResolution
import dev.panini.analysis.KriyaFrame
import dev.panini.analysis.SemanticRelation
import dev.panini.core.Karaka
import dev.panini.core.Vibhakti
import dev.panini.execution.PrakriyaInvocationShape
import dev.panini.execution.prakriyaIdentity
import dev.panini.vyakaranam.ast.SubantaPada

/**
 * PVM's declared procedure call: a registered procedure named in the instrumental
 * is the selected means of कृ, and written locatives are its declared location
 * operands. It requires a call matched against known procedures, so ordinary
 * Sanskrit with कृ and an instrumental receives no such premise.
 */
internal object ProgramProcedureCallConstruction {
    fun infer(call: PrakriyaInvocationShape, frame: KriyaFrame): Map<Int, Set<SemanticRelation>> =
        frame.nominalRelations?.nominalPadas.orEmpty().mapIndexedNotNull { index, nominal ->
            val relation = frame.relations.singleOrNull { it.participant.pada === nominal.pada }
                ?: return@mapIndexedNotNull null
            val unresolved = relation.resolution as? FrameKarakaResolution.Unassigned
                ?: return@mapIndexedNotNull null
            val pada = nominal.pada as? SubantaPada ?: return@mapIndexedNotNull null
            when {
                unresolved.candidates == setOf(Karaka.KARANA) &&
                    nominal.supCandidates.all { it.vibhakti == Vibhakti.TRTIYA } &&
                    pada.pratipadika.prakriyaIdentity() == call.operationStem ->
                    index to setOf(SemanticRelation.INSTRUMENT)
                unresolved.candidates == setOf(Karaka.ADHIKARANA) && nominal.supCandidates.isNotEmpty() &&
                    nominal.supCandidates.all { it.vibhakti == Vibhakti.SAPTAMI } ->
                    index to setOf(SemanticRelation.LOCATION)
                else -> null
            }
        }.toMap()
}
