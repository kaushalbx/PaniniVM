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

/** A declared PVM instruction construes its accusative operands as requested objects. */
internal object ProgramObjectConstruction {
    fun infer(sentence: AkhyataVakya, dhatu: Dhatu?, frame: KriyaFrame): Map<Int, Set<SemanticRelation>> {
        val head = sentence.tinganta
        if (dhatu == null || head.lakara !in setOf(Lakara.LOT, Lakara.LAT) || head.vikarana == Vikarana.YAK ||
            head.upasargaIdentities.any { it == null } || head.dhatu.sanadiAffixes.any { it == null }) return emptyMap()
        val features = GrammaticalFeatures(head.upasargas.toSet(), head.dhatu.sanadiPratyayas.toSet(), lakara = head.lakara)
        if (dhatu.operations.none { operation -> operation.trigger.matches(features) &&
                (operation.signature.requirements.any { it.karaka == Karaka.KARMAN } ||
                    Karaka.KARMAN in operation.signature.optionalKarakas) }) return emptyMap()
        return frame.nominalRelations?.nominalPadas.orEmpty().mapIndexedNotNull { index, nominal ->
            val relation = frame.relations.singleOrNull { it.participant.pada === nominal.pada }
                ?: return@mapIndexedNotNull null
            val unresolved = relation.resolution as? FrameKarakaResolution.Unassigned
            if (unresolved?.candidates != setOf(Karaka.KARMAN) || relation.alternatives.isNotEmpty() ||
                nominal.supCandidates.isEmpty() || nominal.supCandidates.any { it.vibhakti != Vibhakti.DVITIYA } ||
                nominal.lexicalEntry?.semanticRelations.orEmpty().any { it != SemanticRelation.DESIRED_OBJECT })
                return@mapIndexedNotNull null
            index to setOf(SemanticRelation.DESIRED_OBJECT)
        }.toMap()
    }
}
