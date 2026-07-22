package dev.panini.vyakaranam.analysis

import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.Pada

/** Semantic and morphological properties of an individual nominal participant in a sentence. */
data class ParticipantFacts(
    val id: String,
    val expression: Pada,
    val possibleVibhaktis: Set<Vibhakti>,
    val semanticRelations: Set<SemanticRelation> = emptySet(),
)
