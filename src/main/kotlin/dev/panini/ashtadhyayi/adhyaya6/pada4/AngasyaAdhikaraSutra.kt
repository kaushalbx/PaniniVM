package dev.panini.ashtadhyayi.adhyaya6.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.AdhikaraMetadata
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

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
    adhikaraMetadata = AdhikaraMetadata(endKrama = 740097),
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
