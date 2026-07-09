package dev.sanskrit.shiksha

enum class Vyanjana(
    override val devanagari: String,
) : Varna {
    HA("ह"),
    YA("य"),
    VA("व"),
    RA("र"),
    LA("ल"),

    NYA("ञ"),
    MA("म"),
    NGA("ङ"),
    NNA("ण"),
    NA("न"),

    JHA("झ"),
    BHA("भ"),

    GHA("घ"),
    DDHA("ढ"),
    DHA("ध"),

    JA("ज"),
    BA("ब"),
    GA("ग"),
    DDA("ड"),
    DA("द"),

    KHA("ख"),
    PHA("फ"),
    CHA("छ"),
    TTHA("ठ"),
    THA("थ"),
    CA("च"),
    TTA("ट"),
    TA("त"),

    KA("क"),
    PA("प"),

    SHA("श"),
    SSA("ष"),
    SA("स");

    val halanta: String
        get() = devanagari + VIRAMA

    companion object {
        const val VIRAMA: Char = '्'

        fun fromDevanagari(value: Char): Vyanjana? =
            entries.firstOrNull { it.devanagari == value.toString() }
    }
}
