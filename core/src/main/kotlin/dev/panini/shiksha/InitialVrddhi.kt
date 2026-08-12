package dev.panini.shiksha

/** Applies Vṛddhi to the first vowel of a Devanāgarī stem. */
fun applyInitialVrddhi(stem: String): String {
    val value = stem.trim()
    if (value.isEmpty()) return value

    value.forEachIndexed { index, character ->
        INDEPENDENT_VRDDHI[character]?.let { replacement ->
            return value.replaceRange(index, index + 1, replacement)
        }
        if (character !in CONSONANTS || value.getOrNull(index + 1) == VIRAMA) return@forEachIndexed

        val vowelSign = value.getOrNull(index + 1)
        val replacement = vowelSign?.let(DEPENDENT_VRDDHI::get)
        return if (replacement != null) {
            value.replaceRange(index + 1, index + 2, replacement)
        } else {
            // A consonant without a following vowel sign carries an implicit अ.
            value.substring(0, index + 1) + "ा" + value.substring(index + 1)
        }
    }
    return value
}

private const val VIRAMA = '्'
private val INDEPENDENT_VOWELS = setOf('अ', 'आ', 'इ', 'ई', 'उ', 'ऊ', 'ऋ', 'ॠ', 'ए', 'ऐ', 'ओ', 'औ')
private val VOWEL_SIGNS = setOf('ा', 'ि', 'ी', 'ु', 'ू', 'ृ', 'ॄ', 'े', 'ै', 'ो', 'ौ')
private val CONSONANTS = ('क'..'ह').toSet()
private val INDEPENDENT_VRDDHI = mapOf(
    'अ' to "आ",
    'आ' to "आ",
    'इ' to "ऐ",
    'ई' to "ऐ",
    'ए' to "ऐ",
    'ऐ' to "ऐ",
    'उ' to "औ",
    'ऊ' to "औ",
    'ओ' to "औ",
    'औ' to "औ",
    'ऋ' to "आर",
    'ॠ' to "आर",
)
private val DEPENDENT_VRDDHI = mapOf(
    'ा' to "ा",
    'ि' to "ै",
    'ी' to "ै",
    'े' to "ै",
    'ै' to "ै",
    'ु' to "ौ",
    'ू' to "ौ",
    'ो' to "ौ",
    'ौ' to "ौ",
    'ृ' to "ार",
    'ॄ' to "ार",
)
