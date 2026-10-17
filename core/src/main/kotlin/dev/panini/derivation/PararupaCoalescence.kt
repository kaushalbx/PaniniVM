package dev.panini.derivation

import dev.panini.shiksha.Svara

/** Exact two-vowel pararūpa: retain the following vowel's annotations, project
 * both written-sign ledgers, and transfer surviving exact it designations.
 * The consumed term's original form and affix-vowel locus remain in its ledger. */
fun DerivationState.pararupaAtBoundary(leftId: String, rightId: String, sutra: String): DerivationState {
    val index = terms.indexOfFirst { it.id == leftId }
    require(index >= 0 && terms.getOrNull(index + 1)?.id == rightId) {
        "$sutra requires an ordered adjacent pair."
    }
    val left = terms[index]
    val right = terms[index + 1]
    val preceding = left.phonologicalText.effectiveVarnas.lastOrNull()
    val following = right.phonologicalText.effectiveVarnas.firstOrNull()
    require(preceding?.varna is Svara && following?.varna is Svara) {
        "$sutra requires two actual boundary vowels."
    }
    val unchanged = preceding.varna == following.varna && preceding.nasalized == following.nasalized &&
        preceding.accent == following.accent
    val replaced = if (unchanged) this else replaceTermVarna(leftId, left.varnas.lastIndex,
        listOf(following.varna), sutra, annotationDonor = following)
    return replaced.deleteTermVarnas(rightId, 0, 1, sutra)
        .concatenateAfterInitialVowelCoalescence(leftId, right, sutra, transferDeferredDesignations = true)
}
