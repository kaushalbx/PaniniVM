package dev.panini.shiksha

sealed interface Varna {
    val devanagari: String
}

/** Returns a phonological sequence with the varṇa at [index] replaced exactly. */
fun List<Varna>.replaceVarna(index: Int, replacement: List<Varna>): List<Varna> {
    require(index in indices) { "Varṇa index $index is outside a sequence of size $size." }
    return take(index) + replacement + drop(index + 1)
}
