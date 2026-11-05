package dev.panini.execution.binding

import dev.panini.analysis.FrameKarakaResolution
import dev.panini.analysis.KriyaFrame
import dev.panini.analysis.SemanticRelation
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.AkhyataVakya

/**
 * A syncretic dual such as द्वि + ओस् reads as genitive beside an unambiguous genitive in an असँ declaration.
 * Unambiguous genitives need no construction: ordinary analysis reads them as the residual relation of 2.3.50.
 */
internal object ProgramGenitiveConstruction {
    fun infer(sentence: AkhyataVakya, frame: KriyaFrame): Map<Int, Set<SemanticRelation>> {
        val nominals = frame.nominalRelations?.nominalPadas.orEmpty()
        fun relation(index: Int) = frame.relations.singleOrNull { it.participant.pada === nominals[index].pada }
        fun open(index: Int) = relation(index)?.resolution.let {
            it is FrameKarakaResolution.Unassigned || it is FrameKarakaResolution.Ambiguous }
        fun pureGenitive(index: Int) = nominals[index].supCandidates.isNotEmpty() &&
            nominals[index].supCandidates.all { it.vibhakti == Vibhakti.SASTHI }
        val declaration = sentence.tinganta.dhatu.mulaDhatu in setOf("अस्", "असँ") && nominals.indices.any { pureGenitive(it) }
        return nominals.indices.filter { index ->
            open(index) && !pureGenitive(index) && declaration && nominals[index].supCandidates.any { it.vibhakti == Vibhakti.SASTHI } &&
                nominals[index].lexicalEntry?.semanticRelations.isNullOrEmpty()
        }.associateWith { setOf(SemanticRelation.SESHA_RELATION) }
    }
}
