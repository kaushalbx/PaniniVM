package dev.sanskrit.ashtadhyayi.adhyaya6.pada4

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.Samjna
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 
 * 6.4.1: aṅgasya. 
 * This is an adhikāra (governing rule) that spans from 6.4.1 to the end of the 7th adhyāya.
 * It identifies the term before a pratyaya as an 'aṅga'.
 */
object AngasyaAdhikaraSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.4.1",
    text = "अङ्गस्य",
    hindiExplanation = "यह एक अधिकार सूत्र है। यहाँ से सातवें अध्याय के अन्त तक 'अङ्ग' का अधिकार चलता है।",
    type = SutraType.ADHIKARA,
    chapter = 6,
    pada = 4,
    optional = false,
    kramaValue = 640001,
    role = SutraRole.Adhikara,
    action = SutraAction.ADHIKARA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        // Activate if not already active and we have at least two terms (stem + affix)
        return "6.4.1" !in context.activeAdhikaras && context.terms.size >= 2
    }

    override fun apply(context: DerivationState): DerivationChange {
        return DerivationChange(
            state = context.activateAdhikara("6.4.1"),
            explanation = "6.4.1 (Aṅgasya) adhikāra activated."
        )
    }
}
