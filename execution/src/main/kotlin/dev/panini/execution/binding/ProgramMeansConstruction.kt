package dev.panini.execution.binding

import dev.panini.analysis.FrameKarakaResolution
import dev.panini.analysis.KriyaFrame
import dev.panini.analysis.SemanticRelation
import dev.panini.core.Karaka
import dev.panini.core.Lakara
import dev.panini.core.Vibhakti
import dev.panini.dhatupatha.Dhatu
import dev.panini.execution.GrammaticalFeatures
import dev.panini.vyakaranam.ast.AkhyataVakya
import dev.panini.vyakaranam.ast.Vikarana

/**
 * PVM's declared imperative operand construction, not a general Sanskrit inference.
 * A registered instruction with a means slot, a proven object, and one otherwise
 * unassigned instrumental explicitly requests using that operand. No operation
 * is selected and no runtime payload is inspected to manufacture a kāraka.
 */
internal object ProgramMeansConstruction {
    fun infer(sentence: AkhyataVakya, dhatu: Dhatu?, frame: KriyaFrame): Map<Int, Set<SemanticRelation>> {
        val head = sentence.tinganta
        if (dhatu == null || head.lakara != Lakara.LOT || head.vikarana == Vikarana.YAK ||
            head.priorAction != null || head.upasargaIdentities.any { it == null } ||
            head.dhatu.sanadiAffixes.any { it == null }) return emptyMap()
        val features = GrammaticalFeatures(head.upasargas.toSet(), head.dhatu.sanadiPratyayas.toSet(), lakara = head.lakara)
        val declaredMeans = dhatu.operations.any { operation ->
            operation.trigger.matches(features) && (operation.signature.requirements.any { it.karaka == Karaka.KARANA } ||
                Karaka.KARANA in operation.signature.optionalKarakas)
        }
        if (!declaredMeans || frame.relations.none {
                (it.resolution as? FrameKarakaResolution.Resolved)?.karaka == Karaka.KARMAN &&
                    it.evidence.any { proof -> proof.sutra.startsWith("1.4.") }
            }) return emptyMap()
        val operand = frame.relations.filter { relation ->
            val unresolved = relation.resolution as? FrameKarakaResolution.Unassigned
            unresolved?.candidates == setOf(Karaka.KARANA) && relation.alternatives.isEmpty() &&
                relation.participant.supCandidates.all { it.vibhakti == Vibhakti.TRTIYA } &&
                relation.participant.lexicalEntry?.semanticRelations.orEmpty().all { it == SemanticRelation.INSTRUMENT }
        }.singleOrNull()?.participant?.pada ?: return emptyMap()
        val index = frame.nominalRelations?.nominalPadas.orEmpty().indexOfFirst { it.pada === operand }
        return if (index < 0) emptyMap() else mapOf(index to setOf(SemanticRelation.INSTRUMENT))
    }
}
