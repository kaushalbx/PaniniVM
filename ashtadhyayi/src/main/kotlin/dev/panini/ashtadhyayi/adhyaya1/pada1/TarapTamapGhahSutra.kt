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
 * Sūtra 1.1.22 तरप्तपौ घः.
 * Assigns gha saṃjñā to tarap and tamap affixes.
 */
object TarapTamapGhahSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.22", text = "तरप्तपौ घः",
    hindiExplanation = "'तरप्' और 'तमप्' प्रत्ययों की 'घ' संज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 1, optional = false, kramaValue = 110022,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.allEffectiveTerms.any { it.upadesha in setOf("तरप्", "तमप्") } &&
        "1.1.22" !in context.activeAdhikaras

    override fun apply(context: DerivationState): DerivationChange =
        DerivationChange(
            state = context.activateAdhikara("1.1.22"),
            explanation = "1.1.22 assigns घ saṃjñā to tarap and tamap affixes.",
        )
}
