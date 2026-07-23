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
 * 1.1.56: sthānivad ādeśo'nalvidhau.
 * Interpretative rule: A substitute behaves like the original (sthānivat) except in a rule targeting a sound (al-vidhi).
 */
object SthanivadAdesoAnalvidhauSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.56",
    text = "स्थानिवदादेशोऽनल्विधौ",
    hindiExplanation = "आदेशः स्थानिवत् स्यात् न तु अलौ विधाने।",
    type = SutraType.ATIDESHA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110056,
    role = SutraRole.Atidesha,
    action = SutraAction.ATIDESHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = false
    override fun apply(context: DerivationState): DerivationChange = DerivationChange(context, "1.1.56 atideśa applied.")
}
