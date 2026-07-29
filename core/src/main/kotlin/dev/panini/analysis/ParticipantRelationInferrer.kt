package dev.panini.analysis

import dev.panini.core.NominalCategory
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.lexicon.PratipadikaEntry

/** Infers dynamic semantic relations for a nominal participant based on head noun semantics and morphological features. */
object ParticipantRelationInferrer {

    fun infer(
        lexicalEntry: PratipadikaEntry?,
        possibleVibhaktis: Set<Vibhakti>,
        dhatuRelations: Set<SemanticRelation>,
    ): Set<SemanticRelation> = buildSet {
        // 1. Incorporate relations directly specified on the PratipadikaEntry
        lexicalEntry?.semanticRelations?.let { addAll(it) }

        // 2. Incorporate relations from verbal valency expectations
        addAll(dhatuRelations)

        // 3. Dynamic category and morphology-driven inferences
        val categories = lexicalEntry?.categories.orEmpty()

        if (NominalCategory.PLACE_LOCATION in categories || possibleVibhaktis == setOf(Vibhakti.SAPTAMI)) {
            add(SemanticRelation.LOCATION)
        }

        if (NominalCategory.INSTRUMENT_TOOL in categories || possibleVibhaktis == setOf(Vibhakti.TRTIYA)) {
            add(SemanticRelation.INSTRUMENT)
        }

        if (NominalCategory.HUMAN in categories) {
            if (possibleVibhaktis == setOf(Vibhakti.CHATURTHI)) {
                add(SemanticRelation.RECIPIENT)
            }
            if (possibleVibhaktis == setOf(Vibhakti.PRATHAMA) ||
                possibleVibhaktis == setOf(Vibhakti.TRTIYA)
            ) {
                add(SemanticRelation.INDEPENDENT_AGENT)
            }
        }

        if (NominalCategory.OBJECT_ENTITY in categories || possibleVibhaktis == setOf(Vibhakti.DVITIYA)) {
            add(SemanticRelation.DESIRED_OBJECT)
        }
    }
}
