package dev.panini.derivation

/** Insert a new carrier at an explicitly identified owner, without assuming the last term is tiṅ. */
fun DerivationState.insertAfterTerm(ownerId: String, term: DerivationTerm): DerivationState {
    val ownerIndex = terms.indexOfFirst { it.id == ownerId }
    require(ownerIndex >= 0) { "A carrier requires an existing owner: $ownerId" }
    require(terms.none { it.id == term.id }) { "A derivation term id must be unique: ${term.id}" }
    return copy(terms = terms.take(ownerIndex + 1) + term + terms.drop(ownerIndex + 1))
}
