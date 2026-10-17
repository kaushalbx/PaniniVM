package dev.panini.derivation

/** A prescribed choice about an it's grammatical work, not erasure of its designation. */
data class ItMarkerEffectDecision(val enabled: Boolean, val sutra: String) {
    init { require(sutra.isNotBlank()) }
}
