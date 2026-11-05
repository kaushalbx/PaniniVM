package dev.panini.execution.binding

import dev.panini.analysis.KriyaFrame
import dev.panini.analysis.SemanticRelation
import dev.panini.core.KrtAffix
import dev.panini.core.Vibhakti
import dev.panini.dhatupatha.Dhatu
import dev.panini.vyakaranam.ast.KridantaPratipadika
import dev.panini.vyakaranam.ast.SubantaPada

/**
 * धारणाय गणय names what the result is for: a bhāva noun with tumartha under 2.3.15, not a
 * recipient under 1.4.32. It applies only to an instruction declaring a result slot.
 */
internal object ProgramPurposeConstruction {
    private val bhavaAffixes = setOf(KrtAffix.GHAN, KrtAffix.KTIN, KrtAffix.LYUT, KrtAffix.A, KrtAffix.YUC, KrtAffix.KI)

    fun infer(dhatu: Dhatu?, frame: KriyaFrame): Map<Int, Set<SemanticRelation>> {
        if (dhatu == null || dhatu.operations.none { it.resultBindingKaraka == dev.panini.core.Karaka.SAMPRADANA }) return emptyMap()
        return frame.nominalRelations?.nominalPadas.orEmpty().mapIndexedNotNull { index, nominal ->
            val noun = (nominal.pada as? SubantaPada)?.pratipadika as? KridantaPratipadika
            index.takeIf { noun?.krtAffix in bhavaAffixes && nominal.supCandidates.isNotEmpty() &&
                nominal.supCandidates.all { it.vibhakti == Vibhakti.CHATURTHI } }
        }.associateWith { setOf(SemanticRelation.PURPOSE_ACTION, SemanticRelation.ACTION_NOUN_DENOTATION) }
    }
}
