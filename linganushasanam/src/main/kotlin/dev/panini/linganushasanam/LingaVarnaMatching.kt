package dev.panini.linganushasanam

import dev.panini.shiksha.Varna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
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

internal fun LingaRuleContext.hasAkarantaUpadha(expected: Vyanjana): Boolean {
    val stemVarnas = pratipadika.toVarnas()
    val actual = upadha?.toVarnas()?.firstOrNull { it is Vyanjana }
        ?: stemVarnas.dropLast(1).lastOrNull()
    return stemVarnas.lastOrNull() == Svara.A && actual == expected
}
