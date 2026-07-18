package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 1.1.10: nājjhalau.
 * Vowels (ac) and consonants (hal) are not homogeneous (savarṇa) with each other.
 * This is a prohibition (niṣedha) that qualifies 1.1.9.
 */
object NajjhalauSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.10",
    text = "नाज्झलौ",
    hindiExplanation = "अच् (स्वर) और हल् (व्यञ्जन) परस्पर सवर्ण नहीं होते।",
    type = SutraType.NISHEDHA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110010,
    role = SutraRole.Nishedha,
    action = SutraAction.NISHEDHA,
    scope = SutraScope.VARNA,
    blocks = setOf("1.1.9")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = false // Interpretive rule handled by Varnamala

    override fun apply(context: DerivationState): DerivationChange {
        val updated = context.varnaComparisons.map { comparison ->
            if (comparison.leftIsVowel != comparison.rightIsVowel) {
                comparison.copy(forbidden = true)
            } else {
                comparison
            }
        }.toSet()
        return DerivationChange(
            context.copy(varnaComparisons = updated),
            "1.1.10: Vowels and consonants are not savarna."
        )
    }
}
