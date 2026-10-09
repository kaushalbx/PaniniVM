package dev.panini.shiksha

val Svara.isHrasva: Boolean
    get() = this in setOf(Svara.A, Svara.I, Svara.U, Svara.R, Svara.L)

val Svara.isDirgha: Boolean
    get() = !isHrasva

/** The short substitute, including the 1.1.48 mapping of ec to ik. */
fun Svara.toHrasva(): Svara = when (this) {
    Svara.A, Svara.AA -> Svara.A
    Svara.I, Svara.II, Svara.E, Svara.AI -> Svara.I
    Svara.U, Svara.UU, Svara.O, Svara.AU -> Svara.U
    Svara.R, Svara.RR -> Svara.R
    Svara.L, Svara.LL -> Svara.L
}

/** The two-mora savarṇa counterpart; inherently long vowels remain unchanged. */
fun Svara.toDirgha(): Svara = when (this) {
    Svara.A -> Svara.AA
    Svara.I -> Svara.II
    Svara.U -> Svara.UU
    Svara.R -> Svara.RR
    Svara.L -> Svara.LL
    else -> this
}

/** Lengthen the actual final phonological vowel, without altering preceding consonants. */
fun List<Varna>.withFinalDirgha(): List<Varna> {
    val final = requireNotNull(lastOrNull() as? Svara) { "Final-vowel lengthening requires a vowel-final sequence." }
    return dropLast(1) + final.toDirgha()
}

/** The exact phonological guṇa substitute prescribed for an ik vowel. */
fun Svara.toGunaVarnas(): List<Varna> = when (this) {
    Svara.I, Svara.II -> listOf(Svara.E)
    Svara.U, Svara.UU -> listOf(Svara.O)
    Svara.R, Svara.RR -> listOf(Svara.A, Vyanjana.RA)
    Svara.L, Svara.LL -> listOf(Svara.A, Vyanjana.LA)
    else -> error("Guṇa is not defined here for $this.")
}

fun String.withFinalHrasva(): String {
    val final = requireNotNull(lastSvara()) { "A final vowel is required in '$this'." }
    return replaceLastVarna(final, listOf(final.toHrasva()))
}

fun String.withFinalDirgha(): String {
    val final = requireNotNull(lastSvara()) { "A final vowel is required in '$this'." }
    return replaceLastVarna(final, listOf(final.toDirgha()))
}
