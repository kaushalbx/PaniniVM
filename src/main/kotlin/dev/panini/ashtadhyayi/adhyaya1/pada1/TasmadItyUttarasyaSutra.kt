package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.*

/**
 * 1.1.67: tasmād ity uttarasya.
 * Interpretative rule: An operation specified with an Ablative term (tasmāt) applies to the sound immediately following it.
 */
object TasmadItyUttarasyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.67",
    text = "तस्मादित्युत्तरस्य",
    hindiExplanation = "पञ्चमीनिर्देशेन विधीयमानं कार्यं व्यवहितानन्तरस्य परस्य बोध्यम्।",
    type = SutraType.PARIBHASHA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110067,
    role = SutraRole.Paribhasha(targetScope = ParibhashaScope.ABLATIVE_TRIGGER),
    action = SutraAction.PARIBHASHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = false
    override fun apply(context: DerivationState): DerivationChange = DerivationChange(context, "1.1.67 paribhāṣā applied.")
}
