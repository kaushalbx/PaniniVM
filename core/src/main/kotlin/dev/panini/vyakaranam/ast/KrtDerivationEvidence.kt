package dev.panini.vyakaranam.ast

enum class KrtDerivationSense { GENERAL, FUTURE, DEBT, NECESSITY }

/**
 * Established derivational evidence attached to this governor, not its surface
 * ending or the tense of a finite verb. The cited sūtra may govern the domain
 * (e.g. 3.3.3) rather than directly introduce the affix.
 */
data class KrtDerivationEvidence(val governingSutra: String, val sense: KrtDerivationSense) {
    init { require(Regex("[1-8]\\.[1-4]\\.[1-9][0-9]*").matches(governingSutra)) }
}
