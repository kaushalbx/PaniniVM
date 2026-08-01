package dev.panini.ashtadhyayi.adhyaya6.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 6.3.48: त्रेस्त्रयः */
object TreStrayahSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.3.48",
    text = "त्रेस्त्रयः",
    hindiExplanation = "उत्तरपद संख्या होने पर त्रि के स्थान पर त्रयः आदेश होता है।",
    type = SutraType.UTSARGA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630048,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    stage = dev.panini.sutra.SutraStage.ANGAKARYA,
), DerivationSutra {

    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        val purvapada = context.terms.firstOrNull() ?: return false
        val uttarapada = context.terms.getOrNull(1) ?: return false

        val purvaMatch = purvapada.surface == "त्रि"
        val isUttaraSankhya = context.samjnas.any { it.targetId == uttarapada.id && it.samjna == Samjna.SANKHYA } &&
            uttarapada.surface in setOf("दश", "विंशति", "त्रिंशत्")

        return purvaMatch && isUttaraSankhya
    }

    override fun apply(context: DerivationState): DerivationChange {
        val purvapada = context.terms.first()
        val replacement = "त्रयस्"
        val updatedTerm = purvapada.copy(surface = replacement)
        val newTerms = listOf(updatedTerm) + context.terms.drop(1)
        return DerivationChange(
            state = context.copy(terms = newTerms),
            explanation = "$text: ${purvapada.surface} -> $replacement"
        )
    }
}
