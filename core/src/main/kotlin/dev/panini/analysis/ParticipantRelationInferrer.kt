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

        if (NominalCategory.PLACE_LOCATION in categories || Vibhakti.SAPTAMI in possibleVibhaktis) {
            add(SemanticRelation.LOCATION)
        }

        if (NominalCategory.INSTRUMENT_TOOL in categories || Vibhakti.TRTIYA in possibleVibhaktis) {
            add(SemanticRelation.INSTRUMENT)
        }

        if (NominalCategory.HUMAN in categories) {
            if (Vibhakti.CHATURTHI in possibleVibhaktis) {
                add(SemanticRelation.RECIPIENT)
            }
            if (Vibhakti.PRATHAMA in possibleVibhaktis || Vibhakti.TRTIYA in possibleVibhaktis) {
                add(SemanticRelation.INDEPENDENT_AGENT)
            }
        }

        if (NominalCategory.OBJECT_ENTITY in categories || Vibhakti.DVITIYA in possibleVibhaktis) {
            add(SemanticRelation.DESIRED_OBJECT)
        }
    }
}
