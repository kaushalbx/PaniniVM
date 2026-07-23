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
 * 1.1.66: tasminniti nirdiṣṭe pūrvasya.
 * Interpretative rule: An operation specified with a Locative term (tasmin) applies to the sound immediately preceding it.
 */
object TasminnitiNirdishtePurvasyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.66",
    text = "तस्मिन्निति निर्दिष्टे पूर्वस्य",
    hindiExplanation = "सप्तमीनिर्देशेन विधीयमानं कार्यं व्यवहितानन्तरस्य पूर्वस्य बोध्यम्।",
    type = SutraType.PARIBHASHA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110066,
    role = SutraRole.Paribhasha,
    action = SutraAction.PARIBHASHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = false
    override fun apply(context: DerivationState): DerivationChange = DerivationChange(context, "1.1.66 paribhāṣā applied.")
}
