package dev.panini.derivation

import dev.panini.shiksha.Varnamala

/** Orthographic carriers of vowels, including an inherent अ on an unvowelled consonant. */
object DevanagariVowelLoci {
    private val independent = setOf('अ', 'आ', 'इ', 'ई', 'उ', 'ऊ', 'ऋ', 'ॠ', 'ऌ', 'ए', 'ऐ', 'ओ', 'औ')
    private val matras = setOf('ा', 'ि', 'ी', 'ु', 'ू', 'ृ', 'ॄ', 'ॢ', 'े', 'ै', 'ो', 'ौ')

    fun positions(text: String): List<Int> = buildList {
        text.indices.forEach { index ->
            val character = text[index]
            when {
                character in independent || character in matras -> add(index)
                Varnamala.isConsonant(character) && text.getOrNull(index + 1) != '्' && text.getOrNull(index + 1) !in matras -> add(index)
            }
        }
    }
}
