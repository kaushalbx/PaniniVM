package dev.panini.shiksha

/** A written sign that is deliberately not part of the phonological varṇa sequence. */
enum class OrthographicSign(val devanagari: String) {
    AVAGRAHA("ऽ"),
    CHANDRABINDU("ँ"),
}

/** Places [sign] after [afterVarnaCount] phonological tokens. */
data class OrthographicSignPlacement(
    val sign: OrthographicSign,
    val afterVarnaCount: Int,
)

/** Renders annotated tokens and explicit signs without duplicating token nasalization. */
fun SanskritText.renderWithOrthographicSigns(placements: List<OrthographicSignPlacement>): String {
    val tokens = effectiveVarnas
    require(placements.all { it.afterVarnaCount in 0..tokens.size })
    val signs = placements.filterNot {
        it.sign == OrthographicSign.CHANDRABINDU && tokens.getOrNull(it.afterVarnaCount - 1)?.nasalized == true
    }.sortedBy { it.afterVarnaCount }
    return buildString {
        var start = 0
        signs.groupBy { it.afterVarnaCount }.forEach { (boundary, group) ->
            append(SanskritText(tokens.subList(start, boundary)).render())
            group.forEach { append(it.sign.devanagari) }
            start = boundary
        }
        append(SanskritText(tokens.subList(start, tokens.size)).render())
    }
}

fun List<Varna>.toDevanagari(placements: List<OrthographicSignPlacement>): String {
    if (placements.isEmpty()) return toDevanagari()
    val ordered = placements.sortedBy(OrthographicSignPlacement::afterVarnaCount)
    require(ordered.all { it.afterVarnaCount in 0..size }) {
        "Orthographic-sign placement must lie on a varṇa boundary."
    }
    require(ordered.none { placement ->
        val boundary = placement.afterVarnaCount
        boundary in 1 until size && this[boundary - 1] is Vyanjana && this[boundary] is Svara
    }) { "An orthographic sign cannot split a consonant from its following vowel." }

    return buildString {
        var start = 0
        ordered.groupBy(OrthographicSignPlacement::afterVarnaCount).forEach { (boundary, signs) ->
            append(subList(start, boundary).toDevanagari())
            signs.forEach { append(it.sign.devanagari) }
            start = boundary
        }
        append(subList(start, size).toDevanagari())
    }
}
