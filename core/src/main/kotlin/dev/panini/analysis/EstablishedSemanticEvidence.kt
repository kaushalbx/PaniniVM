package dev.panini.analysis

/** Separates supplied/lexical role meaning from case, category and voice hypotheses. */
fun ParticipantFacts.hasEstablishedMeaning(relation: SemanticRelation): Boolean {
    return meaningEvidenceStrength(relation) > 0
}

/** Explicit clause readings outrank lexical defaults and distributed verbal valency. */
fun ParticipantFacts.meaningEvidenceStrength(relation: SemanticRelation): Int {
    if (relation !in semanticRelations) return 0
    val records = semanticEvidence.filter { it.relation == relation }
    // Unattributed facts in the low-level API are explicitly supplied premises.
    if (records.isEmpty()) return 3
    return records.maxOf {
        when (it.source) {
            SemanticEvidenceSource.CLAUSE_CONTEXT -> 3
            SemanticEvidenceSource.LEXICAL_ENTRY -> 2
            SemanticEvidenceSource.DHATU_VALENCY -> 1
            else -> 0
        }
    }
}

fun ParticipantFacts.isSelectedInstrument(): Boolean = meaningEvidenceStrength(SemanticRelation.INSTRUMENT) >= 2 &&
    meaningEvidenceStrength(SemanticRelation.INSTRUMENT) >= meaningEvidenceStrength(SemanticRelation.INDEPENDENT_AGENT)

/** Evidence comparison among ordinary readings, independent of sūtra precedence. */
fun ParticipantFacts.isStrongestOrdinaryReading(relation: SemanticRelation): Boolean =
    meaningEvidenceStrength(relation) >= listOf(SemanticRelation.INDEPENDENT_AGENT,
        SemanticRelation.DESIRED_OBJECT, SemanticRelation.INSTRUMENT, SemanticRelation.LOCATION,
        SemanticRelation.SOURCE, SemanticRelation.RECIPIENT,
        SemanticRelation.SECONDARY_OBJECT, SemanticRelation.AMBIGUOUS_DVIKARMAKA_OBJECT)
        .maxOf(::meaningEvidenceStrength)
