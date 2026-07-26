package dev.panini.analysis

import dev.panini.core.Linga
import dev.panini.core.NominalCategory
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.Pada

/** Semantic and morphological properties of an individual nominal participant in a sentence. */
data class ParticipantFacts(
    val id: String,
    val expression: Pada,
    val possibleVibhaktis: Set<Vibhakti>,
    val semanticRelations: Set<SemanticRelation> = emptySet(),
    val linga: Set<Linga> = emptySet(),
    val vacana: Vacana? = null,
    val categories: Set<NominalCategory> = emptySet(),
)
