package dev.sanskrit.derivation

/** The grammar-bearing changes caused by one applied sūtra. */
data class DerivationDelta(
    val changedTerms: List<TermDelta>,
    val addedTerms: List<DerivationTerm>,
    val removedTerms: List<DerivationTerm>,
    val addedSamjnas: Set<SamjnaAssignment>,
    val removedSamjnas: Set<SamjnaAssignment>,
    val stageBefore: DerivationStage,
    val stageAfter: DerivationStage,
) {
    companion object {
        fun between(before: DerivationState, after: DerivationState): DerivationDelta {
            val beforeById = before.terms.associateBy { it.id }
            val afterById = after.terms.associateBy { it.id }
            return DerivationDelta(
                changedTerms = (beforeById.keys intersect afterById.keys)
                    .mapNotNull { id ->
                        val previous = requireNotNull(beforeById[id])
                        val current = requireNotNull(afterById[id])
                        TermDelta(previous, current).takeIf { previous != current }
                    },
                addedTerms = after.terms.filter { it.id !in beforeById },
                removedTerms = before.terms.filter { it.id !in afterById },
                addedSamjnas = after.samjnas - before.samjnas,
                removedSamjnas = before.samjnas - after.samjnas,
                stageBefore = before.stage,
                stageAfter = after.stage,
            )
        }
    }
}

data class TermDelta(
    val before: DerivationTerm,
    val after: DerivationTerm,
)
