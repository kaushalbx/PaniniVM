package dev.panini.shiksha

/** Replaces one exact phonological ending; written rendering remains a caller boundary. */
fun List<Varna>.replaceExactEnding(ending: List<Varna>, replacement: List<Varna>): List<Varna> {
    require(ending.isNotEmpty()) { "A phonological ending cannot be empty." }
    require(takeLast(ending.size) == ending) { "Expected phonological ending $ending in $this." }
    return dropLast(ending.size) + replacement
}
