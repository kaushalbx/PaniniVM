package dev.panini.analysis

enum class GrammarRegister {
    UNSPECIFIED, CLASSICAL, VEDIC, BRAHMANA;
    val isVedic: Boolean get() = this == VEDIC || this == BRAHMANA
}

/** Supplied construction-specific evidence, not a licence for every word in a Vedic sentence. */
data class VedicCaseLicense(val sutra: String, val reference: String) {
    init {
        require(sutra.isNotBlank() && reference.isNotBlank()) { "A Vedic case licence requires its rule and source reference." }
    }
}

/** Marks licensed forms whose competing observed analyses must be preserved. */
interface AttestedVibhaktiRuleOutcome {
    val attestedCase: dev.panini.core.Vibhakti
}
