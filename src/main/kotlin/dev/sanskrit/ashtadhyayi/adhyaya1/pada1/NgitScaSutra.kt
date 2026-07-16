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
