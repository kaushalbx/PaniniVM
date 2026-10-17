package dev.panini.derivation

import dev.panini.shiksha.Varna

/** A phonological occurrence retains its owning carrier and local token index. */
data class LocatedVarna(val termId: String, val index: Int, val varna: Varna)

/** All eligible consecutive phoneme positions across carriers, never Unicode offsets.
 * Optional selection must not discard independently eligible later occurrences. */
fun DerivationState.findVarnaTriplets(
    eligible: (LocatedVarna, LocatedVarna, LocatedVarna) -> Boolean,
): List<LocatedVarna> {
    if (terms.any { it.itProcessingPending || it.itDesignations.isNotEmpty() || it.deferredItDesignations.isNotEmpty() }) return emptyList()
    val sounds = terms.flatMap { term -> term.varnas.mapIndexed { index, varna -> LocatedVarna(term.id, index, varna) } }
    val targets = mutableListOf<LocatedVarna>()
    for (index in 1 until sounds.lastIndex) {
        if (eligible(sounds[index - 1], sounds[index], sounds[index + 1])) targets += sounds[index]
    }
    return targets
}
