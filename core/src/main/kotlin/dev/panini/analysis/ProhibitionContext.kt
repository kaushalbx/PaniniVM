package dev.panini.analysis

/** Construction evidence, not an inference from the surface endings -aka/-in. */
enum class KrtGenitiveProhibition { AKA_FUTURE_DERIVATION, IN_FUTURE, IN_DEBT }

data class ProhibitionContext(
    val targetSutraNumber: String,
    val affixItMarkers: Set<Char> = emptySet(),
    val isKitOrNgitAffix: Boolean = false,
    val targetPhonemeIsVowel: Boolean = false,
    val secondPhonemeIsConsonant: Boolean = false,
    val isDidhiVeviOrItAugment: Boolean = false,
    val isSetKtvaAffix: Boolean = false,
    val isKrtProhibitedForSasthi: Boolean = false,
    val isAkaFutureOrDebtAffix: Boolean = false,
    val krtGenitiveProhibition: KrtGenitiveProhibition? = null,
    val proposedVibhakti: dev.panini.core.Vibhakti? = null,
)
