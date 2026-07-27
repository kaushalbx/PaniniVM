package dev.panini.katapayadi

import dev.panini.shiksha.Vyanjana

/**
 * Mapping table of Devanagari consonants to Katapayadi single-digit values (0..9).
 */
object KatapayadiMapping {

    fun getDigit(consonant: Char): Int? {
        val vyanjana = Vyanjana.fromDevanagari(consonant) ?: return null
        val ord = vyanjana.ordinal
        return if (ord <= 24) {
            (ord + 1) % 10
        } else {
            ord - 24
        }
    }

    fun isConsonant(ch: Char): Boolean = Vyanjana.fromDevanagari(ch) != null
}
