package dev.panini.derivation

import dev.panini.shiksha.Varna

/** Operative adjacent vowels inside one carrier, excluding surviving hiatus
 * between distinct completed words. The caller supplies its grammatical sounds. */
fun DerivationState.findInternalVowelJunction(eligible: (Varna, Varna) -> Boolean): Pair<DerivationTerm, Int>? {
    for (term in terms) {
        if (term.itProcessingPending || term.itDesignations.isNotEmpty() || term.deferredItDesignations.isNotEmpty()) continue
        for (index in 0 until term.varnas.lastIndex) {
            // Completed lexical input may contain established hiatus (for
            // example an inherited boundary inside a written word). Re-enter
            // internal vowel sandhi only for a junction exposed by a recorded
            // positional change, not for unchanged supplied material.
            if (term.formedPadaRupa != null && substitutions.none { change ->
                change.targetId == term.id && change.sourceVarnaIndex in setOf(index, index + 1) &&
                    change.originalVarnas?.let { original ->
                        original.getOrNull(index) != term.varnas[index] || original.getOrNull(index + 1) != term.varnas[index + 1]
                    } == true
            }) continue
            if (term.completedPadaOrigins.isNotEmpty() &&
                term.completedPadaOrigins[index].intersect(term.completedPadaOrigins[index + 1]).isEmpty()) continue
            if (eligible(term.varnas[index], term.varnas[index + 1])) return term to index
        }
    }
    return null
}
