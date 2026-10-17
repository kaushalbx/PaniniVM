package dev.panini.derivation

import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Varna

/** A term boundary is a pada boundary only when grammar has designated it so. */
fun DerivationState.isPadaFinalVarna(term: DerivationTerm, index: Int): Boolean =
    index in term.varnas.indices && index == term.varnas.lastIndex && samjnas.any {
        it.targetId == term.id && it.samjna in setOf(Samjna.PADA, Samjna.UPASARGA)
    }

/** First sound after a stored term, leaving soundless terms and their provenance intact. */
fun DerivationState.firstVarnaAfterTerm(termId: String): Varna? {
    val index = terms.indexOfFirst { it.id == termId }
    require(index >= 0) { "Unknown phonological term $termId" }
    return terms.asSequence().drop(index + 1).mapNotNull { it.varnas.firstOrNull() }.firstOrNull()
}

/** Completed external padas have their own morphology. A PADA designation on
 * an internal affix alone does not establish a boundary between completed words.
 */
fun DerivationState.hasCompletedPadaBoundaryBetweenTerms(leftId: String, rightId: String): Boolean {
    val left = terms.indexOfFirst { it.id == leftId }
    val right = terms.indexOfFirst { it.id == rightId }
    require(left >= 0 && right >= left) { "Expected ordered phonological terms $leftId and $rightId" }
    return terms.subList(left, right).any { term ->
        term.formedPadaRupa != null && samjnas.any { it.targetId == term.id && it.samjna == Samjna.PADA }
    }
}
