package dev.panini.analysis

import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.SubantaPada

/** Verified lexical slice of the open dūra/antika meaning class. */
object DistanceCaseConstructions {
    private val stems = setOf("दूर", "अन्तिक", "विप्रकृष्ट", "अभ्याश")
    private fun isDistanceWord(pada: SubantaPada?): Boolean =
        (pada?.pratipadika as? MulaPratipadika)?.takeIf { it.vikaras.isEmpty() }?.text in stems

    fun isReference(context: VibhaktiRuleContext): Boolean =
        !isNonSubstanceWord(context) &&
        isDistanceWord(context.participant?.governingNominal) &&
            SemanticRelation.DISTANCE_OR_PROXIMITY in context.participant?.semanticRelations.orEmpty()

    fun isNonSubstanceWord(context: VibhaktiRuleContext): Boolean =
        isDistanceWord(context.participant?.expression as? SubantaPada) &&
            context.participant?.semanticRelations.orEmpty().containsAll(
                setOf(SemanticRelation.DISTANCE_OR_PROXIMITY, SemanticRelation.NON_SUBSTANCE_MEANING))
}
