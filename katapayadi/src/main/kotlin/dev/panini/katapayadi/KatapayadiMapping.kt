package dev.panini.katapayadi

/**
 * Mapping table of Devanagari consonants to Katapayadi single-digit values (0..9).
 */
object KatapayadiMapping {

    private val consonantDigitMap: Map<Char, Int> = mapOf(
        // Digit 1: नञवच कटपय
        'क' to 1, 'ट' to 1, 'प' to 1, 'य' to 1,
        // Digit 2: खठफर
        'ख' to 2, 'ठ' to 2, 'फ' to 2, 'र' to 2,
        // Digit 3: गडबल
        'ग' to 3, 'ड' to 3, 'ब' to 3, 'ल' to 3,
        // Digit 4: घढभव
        'घ' to 4, 'ढ' to 4, 'भ' to 4, 'व' to 4,
        // Digit 5: ङणमश
        'ङ' to 5, 'ण' to 5, 'म' to 5, 'श' to 5,
        // Digit 6: चतष
        'च' to 6, 'त' to 6, 'ष' to 6,
        // Digit 7: छथस
        'छ' to 7, 'थ' to 7, 'स' to 7,
        // Digit 8: जदह
        'ज' to 8, 'द' to 8, 'ह' to 8,
        // Digit 9: झध
        'झ' to 9, 'ध' to 9,
        // Digit 0: ञन
        'ञ' to 0, 'न' to 0
    )

    fun getDigit(consonant: Char): Int? = consonantDigitMap[consonant]

    fun isConsonant(ch: Char): Boolean = ch in consonantDigitMap
}
