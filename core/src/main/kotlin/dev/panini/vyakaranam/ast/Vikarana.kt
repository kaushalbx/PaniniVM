package dev.panini.vyakaranam.ast

import dev.panini.core.DhatuGana
import dev.panini.core.SanadiAffix

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

/**
 * Gaṇa evidence about the original lexical root, not about a derived stem.
 * In the supported present/imperative single-णिच् construction, शप् attaches
 * to the causative stem (3.1.68) and does not reclassify the original root.
 * This lookup constraint is not a complete affix-compatibility validator.
 */
fun TingantaPada.lexicalGanaConstraint(): DhatuGana? =
    if (vikarana == Vikarana.SHAP && dhatu.sanadiAffixes == listOf(SanadiAffix.NIC) &&
        lakara in setOf(dev.panini.core.Lakara.LAT, dev.panini.core.Lakara.LOT)) null
    else vikarana?.gana

/** A known contradiction, not a claim to validate every verbal derivation. */
fun TingantaPada.hasContradictoryActiveCausativeVikarana(): Boolean =
    dhatu.sanadiAffixes == listOf(SanadiAffix.NIC) &&
        lakara in setOf(dev.panini.core.Lakara.LAT, dev.panini.core.Lakara.LOT) &&
        ting.affix?.pada == dev.panini.core.PadaType.PARASMAIPADA &&
        vikarana != null && vikarana != Vikarana.SHAP
