package dev.panini.core

/** Feminine-affix source identities supported by the segmented grammar. */
enum class StriAffix(val upadesha: String) {
    TAP("टाप्"), DAP("डाप्"), CAP("चाप्"), NGIP("ङीप्"), NGISH("ङीष्"),
    NGIN("ङीन्"), UNG("ऊङ्"), TIC("तिच्");

    companion object {
        fun fromUpadesha(text: String): StriAffix? = entries.singleOrNull { it.upadesha == text.trim() }
    }
}
