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
 * 1.1.55: anekāl śit sarvasya.
 * Interpretative rule: A substitute containing multiple phonemes (anekāl) or marked with 'ś' (śit) replaces the entire term.
 */
object AnekalSitSarvasyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.55",
    text = "अनेकाल् शित् सर्वस्य",
    hindiExplanation = "अनेकाल् शित् च आदेशः सर्वस्य स्थानिनः स्यात्।",
    type = SutraType.PARIBHASHA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110055,
    role = SutraRole.Paribhasha,
    action = SutraAction.PARIBHASHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = false
    override fun apply(context: DerivationState): DerivationChange = DerivationChange(context, "1.1.55 paribhāṣā applied.")
}
