package dev.sanskrit.ashtadhyayi.adhyaya1.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.ItMarker
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/**
 * 1.1.52: alo'ntyasya.
 * A substitute replaces only the final sound (al) of the target.
 */
object AlontyasyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.52",
    text = "अलोऽन्त्यस्य",
    hindiExplanation = "आदेश अन्त्य अल (वर्ण) के स्थान पर होता है।",
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
    override fun apply(context: DerivationState): DerivationChange = error("Interpretive rule.")
}

/**
 * 1.1.53: ṅitśca.
 * A substitute marked with 'ṅ' (ṅit) replaces only the final sound, 
 * even if it consists of multiple sounds.
 */
object NgitScaSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.53",
    text = "ङिच्च",
    hindiExplanation = "ङित् आदेश भी अन्त्य अलू के स्थान पर होता है (चाहे वह अनेक अलू वाला हो)।",
    type = SutraType.PARIBHASHA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110053,
    role = SutraRole.Paribhasha,
    action = SutraAction.PARIBHASHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = false
    override fun apply(context: DerivationState): DerivationChange = error("Interpretive rule.")
}

/**
 * 1.1.54: ādeḥ paraṣya.
 * A substitute for a following term replaces only its initial sound (ādi).
 */
object AdehParashyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.54",
    text = "आदेः परस्य",
    hindiExplanation = "पर के स्थान पर होने वाला आदेश उसके आदि वर्ण के स्थान पर होता है।",
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
    override fun apply(context: DerivationState): DerivationChange = error("Interpretive rule.")
}

/**
 * 1.1.55: anekālśit sarvasya.
 * A substitute consisting of multiple sounds or marked with 'ś' (śit) 
 * replaces the entire term.
 */
object AnekalShitSarvasyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.55",
    text = "अनेकाल्शित्सर्वस्य",
    hindiExplanation = "अनेक अल् वाला और शित् आदेश सम्पूर्ण शब्द के स्थान पर होता है।",
    type = SutraType.PARIBHASHA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110055,
    role = SutraRole.Paribhasha,
    action = SutraAction.PARIBHASHA,
    scope = SutraScope.DERIVATION,
    blocks = setOf("1.1.52")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = false
    override fun apply(context: DerivationState): DerivationChange = error("Interpretive rule.")
}
