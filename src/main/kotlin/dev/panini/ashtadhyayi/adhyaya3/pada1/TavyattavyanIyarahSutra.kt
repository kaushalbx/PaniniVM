package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationalMeaning
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.1.96 तव्यत्तव्यानीयरः.
 * Prescribes tavyat, tavya, anīyar kṛtya affixes after roots.
 */
object TavyattavyanIyarahSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.96", text = "तव्यत्तव्यानीयरः",
    hindiExplanation = "धातु से भाव और कर्म अर्थ में 'तव्यत्', 'तव्य' तथा 'अनीयर्' कृत्य प्रत्यय होते हैं।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310096,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == null &&
        context.effectiveContext.requestedMeaning == DerivationalMeaning.BHAVA &&
        context.allEffectiveTerms.none { it.upadesha in setOf("तव्यत्", "तव्य", "अनीयर्") }

    override fun apply(context: DerivationState): DerivationChange {
        val tavyat = DerivationTerm("tavyat", "तव्य", TermKind.PRATYAYA, upadesha = "तव्यत्")
        return DerivationChange(
            state = context.addTerm(tavyat),
            explanation = "3.1.96 prescribes तव्यत् kṛtya affix.",
        )
    }
}
