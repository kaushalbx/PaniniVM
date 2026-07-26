package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.*

/**
 * 1.1.47: midaco'ntyāt paraḥ.
 * An augment marked with 'm' is placed after the last vowel of the term.
 */
object MidacoAntyatParahSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.47",
    text = "मिदचोऽन्त्यात् परः",
    hindiExplanation = "मकार-इत् आगम अन्त्य अच् (स्वर) के बाद बैठता है।",
    type = SutraType.PARIBHASHA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110047,
    role = SutraRole.Paribhasha(targetScope = ParibhashaScope.AUGMENT_PLACEMENT),
    action = SutraAction.PARIBHASHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = false
    override fun apply(context: DerivationState): DerivationChange = error("Interpretive rule.")
}
