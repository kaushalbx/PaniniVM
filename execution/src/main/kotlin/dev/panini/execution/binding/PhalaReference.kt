package dev.panini.execution.binding

import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.MulaPratipadikaIdentity

/** Canonical identity and AST predicate for the prior-result reference फल. */
internal object PhalaReference {
    const val KEY = "फल"
    const val RUNTIME_KEY = "LastResult"

    fun isReference(pada: SubantaPada): Boolean =
        (pada.pratipadika as? MulaPratipadika)?.lexicalIdentity == MulaPratipadikaIdentity.PHALA
}
