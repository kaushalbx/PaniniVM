package dev.panini.ashtadhyayi.adhyaya3.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.4.69 लः कर्मणि च भावे चाकर्मकेभ्यः.
 * Prescribes that Lakāra affixes occur in Kartari for transitives, and in Karmaṇi/Bhāve for intransitives.
 */
object LaKarmaniChaBhaveChakartariChaSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.69", text = "लः कर्मणि च भावे चाकर्मकेभ्यः",
    hindiExplanation = "लकार सकर्मक धातुओं से कर्ता तथा कर्म में, और अकर्मक धातुओं से कर्ता तथा भाव में होते हैं।",
    type = SutraType.NITYA, chapter = 3, pada = 4, optional = false, kramaValue = 340069,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara != null &&
        "3.4.69" !in context.activeAdhikaras

    override fun apply(context: DerivationState): DerivationChange =
        DerivationChange(
            state = context.activateAdhikara("3.4.69"),
            explanation = "3.4.69 establishes lakāra semantics.",
        )
}
