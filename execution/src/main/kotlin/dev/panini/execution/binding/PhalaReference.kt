package dev.panini.execution.binding

import dev.panini.vyakaranam.ast.SubantaPada

/** Canonical identity and AST predicate for the prior-result reference फल. */
internal object PhalaReference {
    const val KEY = "फल"

    fun isReference(pada: SubantaPada): Boolean = pada.pratipadika.baseText() == KEY
}
