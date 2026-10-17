package dev.panini.core

/** Lexical convention in Kāśikā/Nyāsa on 7.1.1: these affixes have a
 * conventionally nasalized yaṇ. This is not inferred from a rendered yu/vu.
 * Other affixes can supply explicit nasalization in their raw upadeśa.
 */
enum class YuvAffixConvention(val upadeshas: Set<String>) {
    YU(setOf("ल्यु", "ल्युट्", "ट्युट्", "ट्युल्")),
    VU(setOf("ण्वुल्", "वुन्", "वुञ्"));

    companion object {
        fun fromUpadesha(upadesha: String): YuvAffixConvention? =
            entries.singleOrNull { upadesha in it.upadeshas }
    }
}
