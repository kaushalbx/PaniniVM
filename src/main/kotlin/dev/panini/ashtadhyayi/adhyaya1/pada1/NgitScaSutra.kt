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
