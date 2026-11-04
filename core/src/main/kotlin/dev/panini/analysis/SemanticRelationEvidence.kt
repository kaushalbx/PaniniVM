package dev.panini.analysis

enum class SemanticEvidenceSource {
    LEXICAL_ENTRY,
    DHATU_VALENCY,
    VOICE_CONSTRUCTION,
    NOMINAL_CATEGORY,
    UNAMBIGUOUS_MORPHOLOGY,
    CONSTRUCTION_AMBIGUITY,
    GOVERNING_CONSTRUCTION,
    CLAUSE_CONTEXT,
}

data class SemanticRelationEvidence(
    val relation: SemanticRelation,
    val source: SemanticEvidenceSource,
    val detail: String,
)
