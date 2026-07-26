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
 * Sūtra 1.1.38 तौ सत्.
 * Assigns sat saṃjñā to śatṛ and śānac affixes.
 */
object SnatSatSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.38", text = "तौ सत्",
    hindiExplanation = "'शतृ' तथा 'शानच्' प्रत्ययों की 'सत्' संज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 1, optional = false, kramaValue = 110038,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.allEffectiveTerms.any { it.upadesha in setOf("शतृ", "शानच्") } &&
        "1.1.38" !in context.activeAdhikaras

    override fun apply(context: DerivationState): DerivationChange =
        DerivationChange(
            state = context.activateAdhikara("1.1.38"),
            explanation = "1.1.38 assigns सत् saṃjñā to śatṛ and śānac affixes.",
        )
}
