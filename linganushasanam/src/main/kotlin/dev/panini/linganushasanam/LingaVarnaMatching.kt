package dev.panini.linganushasanam

import dev.panini.shiksha.Varna
import dev.panini.shiksha.toVarnas

internal fun String.phonologicallyEquals(other: String): Boolean = toVarnas() == other.toVarnas()

internal fun String.phonologicallyIn(candidates: Set<String>): Boolean =
    candidates.any(::phonologicallyEquals)

internal fun String.endsWithVarnas(suffix: String): Boolean {
    val source = toVarnas()
    val ending = suffix.toVarnas()
    return source.size >= ending.size && source.takeLast(ending.size) == ending
}

internal fun List<Varna>.endsWithVarnas(suffix: String): Boolean {
    val ending = suffix.toVarnas()
    return size >= ending.size && takeLast(ending.size) == ending
}
