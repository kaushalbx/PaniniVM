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
 * Sūtra 3.4.67 कर्तरि कृत्.
 * Prescribes that Kṛt affixes occur in Kartari (agent) sense unless otherwise specified.
 */
object KartariKrtSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.67", text = "कर्तरि कृत्",
    hindiExplanation = "विशेष नियम न होने पर 'कृत' प्रत्यय कर्ता अर्थ में होते हैं।",
    type = SutraType.NITYA, chapter = 3, pada = 4, optional = false, kramaValue = 340067,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == null &&
        context.effectiveContext.requestedMeaning != null &&
        "3.4.67" !in context.activeAdhikaras

    override fun apply(context: DerivationState): DerivationChange =
        DerivationChange(
            state = context.activateAdhikara("3.4.67"),
            explanation = "3.4.67 establishes Kartari sense for Kṛt affixes.",
        )
}
