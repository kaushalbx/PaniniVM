package dev.panini.sankhya

enum class PrimitiveSankhya(
    val value: Long,
    val pratipadika: String,
    /** Form supplied as a पूर्वपद before numeral-internal sandhi. */
    val purvapada: String = pratipadika,
    /** Form supplied as an उत्तरपद before numeral-internal sandhi. */
    val uttarapada: String = pratipadika,
    val inflectionClass: SankhyaInflectionClass = SankhyaInflectionClass.SPECIAL,
) {
    SHUNYA(0L, "शून्य"),

    EKA(1L, "एक"),
    DVI(2L, "द्वि"),
    TRI(3L, "त्रि"),
    CHATUR(4L, "चतुर्"),
    PANCHAN(5L, "पञ्चन्", purvapada = "पञ्च", inflectionClass = SankhyaInflectionClass.COUNT_FIVE_TO_NINETEEN),
    SHASH(6L, "षष्", inflectionClass = SankhyaInflectionClass.COUNT_FIVE_TO_NINETEEN),
    SAPTAN(7L, "सप्तन्", purvapada = "सप्त", inflectionClass = SankhyaInflectionClass.COUNT_FIVE_TO_NINETEEN),
    ASHTAN(8L, "अष्टन्", inflectionClass = SankhyaInflectionClass.COUNT_FIVE_TO_NINETEEN),
    NAVAN(9L, "नवन्", purvapada = "नव", inflectionClass = SankhyaInflectionClass.COUNT_FIVE_TO_NINETEEN),

    DASHAN(10L, "दशन्", uttarapada = "दश", inflectionClass = SankhyaInflectionClass.COUNT_FIVE_TO_NINETEEN),
    SHODASHA(16L, "षोडश", inflectionClass = SankhyaInflectionClass.COUNT_FIVE_TO_NINETEEN),

    VIMSHATI(20L, "विंशति", inflectionClass = SankhyaInflectionClass.FEMININE_I),
    TRIMSHAT(30L, "त्रिंशत्", inflectionClass = SankhyaInflectionClass.FEMININE_T),
    CHATVARIMSHAT(40L, "चत्वारिंशत्", inflectionClass = SankhyaInflectionClass.FEMININE_T),
    PANCHASHAT(50L, "पञ्चाशत्", inflectionClass = SankhyaInflectionClass.FEMININE_T),
    SHASHTI(60L, "षष्टि", inflectionClass = SankhyaInflectionClass.FEMININE_I),
    SAPTATI(70L, "सप्तति", inflectionClass = SankhyaInflectionClass.FEMININE_I),
    ASHITI(80L, "अशीति", inflectionClass = SankhyaInflectionClass.FEMININE_I),
    NAVATI(90L, "नवति", inflectionClass = SankhyaInflectionClass.FEMININE_I),

    SHATA(100L, "शत", inflectionClass = SankhyaInflectionClass.NEUTER_A),
    SAHASRA(1_000L, "सहस्र", inflectionClass = SankhyaInflectionClass.NEUTER_A),
    AYUTA(10_000L, "अयुत", inflectionClass = SankhyaInflectionClass.NEUTER_A),
    LAKSHA(100_000L, "लक्ष", inflectionClass = SankhyaInflectionClass.NEUTER_A),
    PRAYUTA(1_000_000L, "प्रयुत", inflectionClass = SankhyaInflectionClass.NEUTER_A),
    KOTI(10_000_000L, "कोटि", inflectionClass = SankhyaInflectionClass.FEMININE_I);

    companion object {
        private val byValue = entries.associateBy(PrimitiveSankhya::value)
        private val byPratipadika = entries.associateBy(PrimitiveSankhya::pratipadika)
        private val annotatedPratipadikas = entries.flatMap { sankhya ->
            setOf(sankhya.pratipadika, sankhya.purvapada, sankhya.uttarapada).map { it to sankhya }
        }.toMap()

        fun fromValue(value: Long): PrimitiveSankhya? = byValue[value]

        fun fromPratipadika(text: String): PrimitiveSankhya? = byPratipadika[text]

        /** Resolves only an explicitly annotated numeral prātipadika, never a surface subanta. */
        fun fromAnnotatedPratipadika(text: String): PrimitiveSankhya? = annotatedPratipadikas[text]
    }
}

enum class SankhyaInflectionClass { SPECIAL, COUNT_FIVE_TO_NINETEEN, FEMININE_I, FEMININE_T, NEUTER_A }
