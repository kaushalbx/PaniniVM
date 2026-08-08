package dev.panini.execution

import dev.panini.core.Linga

/** Nominal compatibility metadata pending complete lexicon-driven declension. */
internal object PvmNominalLexicon {
    private val GENDER_OVERRIDES = mapOf(
        "हविस्" to Linga.NAPUMSAKA,
        "मनस्" to Linga.NAPUMSAKA,
        "पयस्" to Linga.NAPUMSAKA,
        "उरस्" to Linga.NAPUMSAKA,
        "चक्षुस्" to Linga.NAPUMSAKA,
    )
    private val PRESERVED_SURFACES = setOf("क्षीप्", "क्षिप्")

    fun gender(stem: String): Linga = GENDER_OVERRIDES[stem] ?: Linga.PUMS

    fun surface(stem: String, derived: String): String =
        if (stem in PRESERVED_SURFACES) stem else derived
}
