package dev.panini.analysis

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
)
