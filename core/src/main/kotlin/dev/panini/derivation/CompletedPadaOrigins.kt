package dev.panini.derivation

/** Exact ownership of phonological occurrences, independent of rendered word
 * boundaries. A coalesced occurrence may belong to both original padas. */
fun DerivationTerm.padaOriginsAt(index: Int): Set<String> {
    require(index in varnas.indices)
    if (completedPadaOrigins.isEmpty()) return emptySet()
    require(completedPadaOrigins.size == varnas.size) { "Stale pada-origin projection on $id" }
    return completedPadaOrigins[index]
}

internal fun DerivationTerm.projectPadaOrigins(from: Int, count: Int, replacementSize: Int): List<Set<String>> {
    if (completedPadaOrigins.isEmpty()) return emptyList()
    require(completedPadaOrigins.size == varnas.size && from >= 0 && count >= 0 && from + count <= varnas.size)
    val inherited = if (count > 0) completedPadaOrigins.subList(from, from + count).flatten().toSet()
        else completedPadaOrigins.getOrNull(from) ?: completedPadaOrigins.lastOrNull().orEmpty()
    return completedPadaOrigins.take(from) + List(replacementSize) { inherited } + completedPadaOrigins.drop(from + count)
}

fun DerivationState.areInSameCompletedPada(left: DerivationTerm, leftIndex: Int,
    right: DerivationTerm, rightIndex: Int): Boolean {
    val leftOrigins = left.padaOriginsAt(leftIndex)
    val rightOrigins = right.padaOriginsAt(rightIndex)
    if (leftOrigins.isNotEmpty() && rightOrigins.isNotEmpty()) return leftOrigins.any { it in rightOrigins }
    // Internal derivations have not handed off completed padas. Their existing
    // grammatical PADA endpoints, rather than term boundaries alone, apply.
    return !hasCompletedPadaBoundaryBetweenTerms(left.id, right.id)
}
