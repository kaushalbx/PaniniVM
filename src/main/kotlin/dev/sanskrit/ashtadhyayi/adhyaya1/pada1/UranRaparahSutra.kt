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
 * 1.1.51: uraṇ raparaḥ.
 * An 'aṇ' vowel (a, i, u) that comes as a substitute for 'ṛ' (ṛ-varṇa) 
 * is always followed by 'r'.
 */
object UranRaparahSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.51",
    text = "उरण् रपरः",
    hindiExplanation = "ऋ वर्ण के स्थान पर होने वाला अण् (अ, इ, उ) 'र्' के साथ (रपर होकर) प्रवृत्त होता है।",
    type = SutraType.PARIBHASHA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110051,
    role = SutraRole.Paribhasha,
    action = SutraAction.PARIBHASHA,
    scope = SutraScope.VARNA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = false
    override fun apply(context: DerivationState): DerivationChange = error("Interpretive rule.")
}
