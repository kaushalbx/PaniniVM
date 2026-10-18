package dev.panini.ashtadhyayi.adhyaya3.pada2

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 3.2.127 तौ सत्: assigns sat designation to the śatṛ and śānac affixes. */
object TauSatSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.2.127", text = "तौ सत्",
    hindiExplanation = "शतृँ तथा शानच् प्रत्ययों की सत् संज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 3, pada = 2, optional = false, kramaValue = 320127,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.allEffectiveTerms.any { it.upadesha in setOf("शतृँ", "शानच्") } &&
            number !in context.activeAdhikaras

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.activateAdhikara(number),
        "3.2.127 assigns सत् designation to śatṛ and śānac affixes.",
    )
}
