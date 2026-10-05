package dev.panini.vyakaranam.ast

import dev.panini.core.DhatuGana

/** Source affix identity, not its post-it-lopa surface. Not every affix selects a gaṇa. */
enum class Vikarana(val upadesha: String, val gana: DhatuGana? = null) {
    SHAP("शप्", DhatuGana.BHVADI),
    SHYAN("श्यन्", DhatuGana.DIVADI),
    SHNU("श्नु", DhatuGana.SVADI),
    SHNAM("श्नम्", DhatuGana.RUDHADI),
    SHNA("श्ना", DhatuGana.KRYADI),
    U("उ", DhatuGana.TANADI),
    YAK("यक्"),
    SHA("शः", DhatuGana.TUDADI),
    SYA("स्य"),
    TAS("तास्"),
    CLI("च्लि"),
    SIC("सिच्"),
    ANG("अङ्"),
    CHANG("चङ्"),
    KSA("क्स"),
    ;

    companion object {
        fun fromUpadesha(text: String): Vikarana = entries.singleOrNull { it.upadesha == text }
            ?: error("Unsupported vikaraṇa upadeśa: '$text'.")
    }
}
