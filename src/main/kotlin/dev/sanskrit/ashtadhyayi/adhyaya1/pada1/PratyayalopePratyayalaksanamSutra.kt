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
 * 1.1.62: pratyayalope pratyayalakṣaṇam.
 * Even when an affix is deleted (lopa), the operation that depends on that affix 
 * still takes place.
 */
object PratyayalopePratyayalaksanamSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.62",
    text = "प्रत्ययलोपे प्रत्ययलक्षणम्",
    hindiExplanation = "प्रत्यय का लोप होने पर भी उस प्रत्यय पर आश्रित कार्य होते हैं।",
    type = SutraType.PARIBHASHA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110062,
    role = SutraRole.Paribhasha,
    action = SutraAction.PARIBHASHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {

    override fun matches(context: DerivationState): Boolean = false // Interpretive rule

    override fun apply(context: DerivationState): DerivationChange =
        error("Paribhasha sutra 1.1.62 should not be applied directly.")
}
