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
 * 1.1.52: alo'ntyasya.
 * Interpretative rule: Substitution prescribed for a term replaces only the last sound (al) of that term.
 */
object AlAntyasyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.52",
    text = "आलोऽन्त्यस्य",
    hindiExplanation = "षष्ठीनिर्दिष्टस्यान्त्यस्याल आदेशः स्यात्।",
    type = SutraType.PARIBHASHA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110052,
    role = SutraRole.Paribhasha,
    action = SutraAction.PARIBHASHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = false
    override fun apply(context: DerivationState): DerivationChange = DerivationChange(context, "1.1.52 paribhāṣā applied.")
}
