package dev.panini.sankhya

import java.math.BigInteger

enum class PrimitiveSankhya(
    val value: BigInteger,
    val pratipadika: String,
    /** Form supplied as a पूर्वपद before numeral-internal sandhi. */
    val purvapada: String = pratipadika,
    /** Form supplied as an उत्तरपद before numeral-internal sandhi. */
    val uttarapada: String = pratipadika,
) {
    SHUNYA(BigInteger.ZERO, "शून्य"),

    EKA(BigInteger.ONE, "एक"),
    DVI(BigInteger.TWO, "द्वि"),
    TRI(BigInteger.valueOf(3), "त्रि"),
    CHATUR(BigInteger.valueOf(4), "चतुर्"),
    PANCHAN(BigInteger.valueOf(5), "पञ्चन्", purvapada = "पञ्च"),
    SHASH(BigInteger.valueOf(6), "षष्"),
    SAPTAN(BigInteger.valueOf(7), "सप्तन्", purvapada = "सप्त"),
    ASHTAN(BigInteger.valueOf(8), "अष्टन्"),
    NAVAN(BigInteger.valueOf(9), "नवन्", purvapada = "नव"),

    DASHAN(BigInteger.TEN, "दशन्", uttarapada = "दश"),
    SHODASHA(BigInteger.valueOf(16), "षोडश"),

    VIMSHATI(BigInteger.valueOf(20), "विंशति"),
    TRIMSHAT(BigInteger.valueOf(30), "त्रिंशत्"),
    CHATVARIMSHAT(BigInteger.valueOf(40), "चत्वारिंशत्"),
    PANCHASHAT(BigInteger.valueOf(50), "पञ्चाशत्"),
    SHASHTI(BigInteger.valueOf(60), "षष्टि"),
    SAPTATI(BigInteger.valueOf(70), "सप्तति"),
    ASHITI(BigInteger.valueOf(80), "अशीति"),
    NAVATI(BigInteger.valueOf(90), "नवति"),

    SHATA(BigInteger.valueOf(100), "शत"),
    SAHASRA(BigInteger.valueOf(1_000), "सहस्र"),
    AYUTA(BigInteger.valueOf(10_000), "अयुत"),
    LAKSHA(BigInteger.valueOf(100_000), "लक्ष"),
    PRAYUTA(BigInteger.valueOf(1_000_000), "प्रयुत"),
    KOTI(BigInteger.valueOf(10_000_000), "कोटि");

    companion object {
        private val byValue = entries.associateBy(PrimitiveSankhya::value)
        private val byPratipadika = entries.associateBy(PrimitiveSankhya::pratipadika)

        fun fromValue(value: BigInteger): PrimitiveSankhya? = byValue[value]

        fun fromPratipadika(text: String): PrimitiveSankhya? = byPratipadika[text]
    }
}
