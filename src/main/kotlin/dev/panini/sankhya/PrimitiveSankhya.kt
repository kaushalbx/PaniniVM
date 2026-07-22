package dev.panini.sankhya

import java.math.BigInteger

enum class PrimitiveSankhya(
    val value: BigInteger,
    val pratipadika: String,
    /** Form supplied as a पूर्वपद before numeral-internal sandhi. */
    val purvapada: String = pratipadika,
    /** Form supplied as an उत्तरपद before numeral-internal sandhi. */
    val uttarapada: String = pratipadika,
    val inflectionClass: SankhyaInflectionClass = SankhyaInflectionClass.SPECIAL,
) {
    SHUNYA(BigInteger.ZERO, "शून्य"),

    EKA(BigInteger.ONE, "एक"),
    DVI(BigInteger.TWO, "द्वि"),
    TRI(BigInteger.valueOf(3), "त्रि"),
    CHATUR(BigInteger.valueOf(4), "चतुर्"),
    PANCHAN(BigInteger.valueOf(5), "पञ्चन्", purvapada = "पञ्च", inflectionClass = SankhyaInflectionClass.COUNT_FIVE_TO_NINETEEN),
    SHASH(BigInteger.valueOf(6), "षष्", inflectionClass = SankhyaInflectionClass.COUNT_FIVE_TO_NINETEEN),
    SAPTAN(BigInteger.valueOf(7), "सप्तन्", purvapada = "सप्त", inflectionClass = SankhyaInflectionClass.COUNT_FIVE_TO_NINETEEN),
    ASHTAN(BigInteger.valueOf(8), "अष्टन्", inflectionClass = SankhyaInflectionClass.COUNT_FIVE_TO_NINETEEN),
    NAVAN(BigInteger.valueOf(9), "नवन्", purvapada = "नव", inflectionClass = SankhyaInflectionClass.COUNT_FIVE_TO_NINETEEN),

    DASHAN(BigInteger.TEN, "दशन्", uttarapada = "दश", inflectionClass = SankhyaInflectionClass.COUNT_FIVE_TO_NINETEEN),
    SHODASHA(BigInteger.valueOf(16), "षोडश", inflectionClass = SankhyaInflectionClass.COUNT_FIVE_TO_NINETEEN),

    VIMSHATI(BigInteger.valueOf(20), "विंशति", inflectionClass = SankhyaInflectionClass.FEMININE_I),
    TRIMSHAT(BigInteger.valueOf(30), "त्रिंशत्", inflectionClass = SankhyaInflectionClass.FEMININE_T),
    CHATVARIMSHAT(BigInteger.valueOf(40), "चत्वारिंशत्", inflectionClass = SankhyaInflectionClass.FEMININE_T),
    PANCHASHAT(BigInteger.valueOf(50), "पञ्चाशत्", inflectionClass = SankhyaInflectionClass.FEMININE_T),
    SHASHTI(BigInteger.valueOf(60), "षष्टि", inflectionClass = SankhyaInflectionClass.FEMININE_I),
    SAPTATI(BigInteger.valueOf(70), "सप्तति", inflectionClass = SankhyaInflectionClass.FEMININE_I),
    ASHITI(BigInteger.valueOf(80), "अशीति", inflectionClass = SankhyaInflectionClass.FEMININE_I),
    NAVATI(BigInteger.valueOf(90), "नवति", inflectionClass = SankhyaInflectionClass.FEMININE_I),

    SHATA(BigInteger.valueOf(100), "शत", inflectionClass = SankhyaInflectionClass.NEUTER_A),
    SAHASRA(BigInteger.valueOf(1_000), "सहस्र", inflectionClass = SankhyaInflectionClass.NEUTER_A),
    AYUTA(BigInteger.valueOf(10_000), "अयुत", inflectionClass = SankhyaInflectionClass.NEUTER_A),
    LAKSHA(BigInteger.valueOf(100_000), "लक्ष", inflectionClass = SankhyaInflectionClass.NEUTER_A),
    PRAYUTA(BigInteger.valueOf(1_000_000), "प्रयुत", inflectionClass = SankhyaInflectionClass.NEUTER_A),
    KOTI(BigInteger.valueOf(10_000_000), "कोटि", inflectionClass = SankhyaInflectionClass.FEMININE_I);

    companion object {
        private val byValue = entries.associateBy(PrimitiveSankhya::value)
        private val byPratipadika = entries.associateBy(PrimitiveSankhya::pratipadika)

        fun fromValue(value: BigInteger): PrimitiveSankhya? = byValue[value]

        fun fromPratipadika(text: String): PrimitiveSankhya? = byPratipadika[text]
    }
}

enum class SankhyaInflectionClass { SPECIAL, COUNT_FIVE_TO_NINETEEN, FEMININE_I, FEMININE_T, NEUTER_A }
