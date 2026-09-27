package dev.panini.shiksha

/** A written sign that is deliberately not part of the phonological varṇa sequence. */
enum class OrthographicSign(val devanagari: String) {
    AVAGRAHA("ऽ"),
    CHANDRABINDU("ँ"),
}

/** Exact written span of a vowel followed by candrabindu in an upadeśa. */
data class NasalizedVowelOrthographicSpan(
    val start: Int,
    val endExclusive: Int,
    val dependentVowelSign: Boolean,
    val text: String,
)

/** Parses non-phonological written boundaries needed to retain exact इत् provenance. */
fun String.nasalizedVowelOrthographicSpans(): List<NasalizedVowelOrthographicSpan> =
    indices.filter { this[it] == OrthographicSign.CHANDRABINDU.devanagari.single() }.mapNotNull { signIndex ->
        val vowelIndex = signIndex - 1
        if (vowelIndex < 0) return@mapNotNull null
        val dependent = Svara.entries.any { it.matra == this[vowelIndex].toString() }
        val independent = Svara.fromIndependent(this[vowelIndex]) != null
        if (!dependent && !independent) return@mapNotNull null
        NasalizedVowelOrthographicSpan(
            start = vowelIndex,
            endExclusive = signIndex + 1,
            dependentVowelSign = dependent,
            text = substring(vowelIndex, signIndex + 1),
        )
    }

/** Places [sign] after [afterVarnaCount] phonological tokens. */
data class OrthographicSignPlacement(
    val sign: OrthographicSign,
    val afterVarnaCount: Int,
)

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

/**
 * Canonicalizes a written boundary exposed after exact-span इत् lopa.
 * This belongs to the orthographic renderer; grammatical rules never inspect
 * the mātrā or virāma spellings represented here.
 */
fun String.joinDevanagariVowelBoundary(): String {
    val vowelSigns = linkedMapOf(
        "्अ" to "", "्आ" to "ा", "्इ" to "ि", "्ई" to "ी",
        "्उ" to "ु", "्ऊ" to "ू", "्ऋ" to "ृ", "्ॠ" to "ॄ",
        "्ऌ" to "ॢ", "्ए" to "े", "्ऐ" to "ै", "्ओ" to "ो", "्औ" to "ौ",
    )
    return vowelSigns.entries.fold(this) { value, (boundary, sign) -> value.replace(boundary, sign) }
}
