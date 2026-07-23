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
 * 1.1.54: ādeḥ parasya.
 * Interpretative rule: Substitution prescribed for an element following an Ablative term replaces the first sound (ādi) of that element.
 */
object AdehParasyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.54",
    text = "आदेः परस्य",
    hindiExplanation = "परस्य यद्विहितं तत्तस्यादेरालो बोध्यम्।",
    type = SutraType.PARIBHASHA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110054,
    role = SutraRole.Paribhasha,
    action = SutraAction.PARIBHASHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = false
    override fun apply(context: DerivationState): DerivationChange = DerivationChange(context, "1.1.54 paribhāṣā applied.")
}
