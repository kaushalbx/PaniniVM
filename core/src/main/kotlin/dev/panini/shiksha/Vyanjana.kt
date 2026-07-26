package dev.panini.shiksha

enum class Vyanjana(
    override val devanagari: String,
    val sthana: Set<Sthana>,
    val abhyantaraPrayatna: AbhyantaraPrayatna
) : Varna {
    KA("क", setOf(Sthana.KANTHA), AbhyantaraPrayatna.SPRSTA),
    KHA("ख", setOf(Sthana.KANTHA), AbhyantaraPrayatna.SPRSTA),
    GA("ग", setOf(Sthana.KANTHA), AbhyantaraPrayatna.SPRSTA),
    GHA("घ", setOf(Sthana.KANTHA), AbhyantaraPrayatna.SPRSTA),
    NGA("ङ", setOf(Sthana.KANTHA, Sthana.NASIKA), AbhyantaraPrayatna.SPRSTA),

    CA("च", setOf(Sthana.TALU), AbhyantaraPrayatna.SPRSTA),
    CHA("छ", setOf(Sthana.TALU), AbhyantaraPrayatna.SPRSTA),
    JA("ज", setOf(Sthana.TALU), AbhyantaraPrayatna.SPRSTA),
    JHA("झ", setOf(Sthana.TALU), AbhyantaraPrayatna.SPRSTA),
    NYA("ञ", setOf(Sthana.TALU, Sthana.NASIKA), AbhyantaraPrayatna.SPRSTA),

    TTA("ट", setOf(Sthana.MURDHA), AbhyantaraPrayatna.SPRSTA),
    TTHA("ठ", setOf(Sthana.MURDHA), AbhyantaraPrayatna.SPRSTA),
    DDA("ड", setOf(Sthana.MURDHA), AbhyantaraPrayatna.SPRSTA),
    DDHA("ढ", setOf(Sthana.MURDHA), AbhyantaraPrayatna.SPRSTA),
    NNA("ण", setOf(Sthana.MURDHA, Sthana.NASIKA), AbhyantaraPrayatna.SPRSTA),

    TA("त", setOf(Sthana.DANTA), AbhyantaraPrayatna.SPRSTA),
    THA("थ", setOf(Sthana.DANTA), AbhyantaraPrayatna.SPRSTA),
    DA("द", setOf(Sthana.DANTA), AbhyantaraPrayatna.SPRSTA),
    DHA("ध", setOf(Sthana.DANTA), AbhyantaraPrayatna.SPRSTA),
    NA("न", setOf(Sthana.DANTA, Sthana.NASIKA), AbhyantaraPrayatna.SPRSTA),

    PA("प", setOf(Sthana.OSTHA), AbhyantaraPrayatna.SPRSTA),
    PHA("फ", setOf(Sthana.OSTHA), AbhyantaraPrayatna.SPRSTA),
    BA("ब", setOf(Sthana.OSTHA), AbhyantaraPrayatna.SPRSTA),
    BHA("भ", setOf(Sthana.OSTHA), AbhyantaraPrayatna.SPRSTA),
    MA("म", setOf(Sthana.OSTHA, Sthana.NASIKA), AbhyantaraPrayatna.SPRSTA),

    YA("य", setOf(Sthana.TALU), AbhyantaraPrayatna.ISAT_SPRSTA),
    RA("र", setOf(Sthana.MURDHA), AbhyantaraPrayatna.ISAT_SPRSTA),
    LA("ल", setOf(Sthana.DANTA), AbhyantaraPrayatna.ISAT_SPRSTA),
    VA("व", setOf(Sthana.DANTOSTHA), AbhyantaraPrayatna.ISAT_SPRSTA),

    SHA("श", setOf(Sthana.TALU), AbhyantaraPrayatna.ISAT_VIVRTA),
    SSA("ष", setOf(Sthana.MURDHA), AbhyantaraPrayatna.ISAT_VIVRTA),
    SA("स", setOf(Sthana.DANTA), AbhyantaraPrayatna.ISAT_VIVRTA),
    HA("ह", setOf(Sthana.KANTHA), AbhyantaraPrayatna.ISAT_VIVRTA);

    val halanta: String
        get() = devanagari + VIRAMA

    companion object {
        const val VIRAMA: Char = '्'

        fun fromDevanagari(value: Char): Vyanjana? =
            entries.firstOrNull { it.devanagari == value.toString() }
    }
}
