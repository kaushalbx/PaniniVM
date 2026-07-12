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
 * 1.1.46: ādyantau ṭakitau.
 * An augment (āgama) marked with 'ṭ' is placed at the beginning of the term.
 * An augment marked with 'k' is placed at the end of the term.
 */
object AdyantauTakitauSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.46",
    text = "आद्यन्तौ टकितौ",
    hindiExplanation = "टकार-इत् आगम आदि में और ककार-इत् आगम अन्त में जुड़ता है।",
    type = SutraType.PARIBHASHA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110046,
    role = SutraRole.Paribhasha,
    action = SutraAction.PARIBHASHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = false
    override fun apply(context: DerivationState): DerivationChange = error("Interpretive rule.")
}

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
    role = SutraRole.Paribhasha,
    action = SutraAction.PARIBHASHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = false
    override fun apply(context: DerivationState): DerivationChange = error("Interpretive rule.")
}
