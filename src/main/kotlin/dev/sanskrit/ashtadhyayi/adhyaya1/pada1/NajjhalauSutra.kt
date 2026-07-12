package dev.sanskrit.ashtadhyayi.adhyaya1.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

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

    override fun apply(context: DerivationState): DerivationChange =
        error("Nishedha sutra 1.1.10 is interpretive and should not be applied directly.")
}
