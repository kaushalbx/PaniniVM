package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.derivation.DerivationChange
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
 * Sūtra 3.1.144 गेहे कः.
 * Prescribes ka affix for grah root in house sense.
 */
object GeheKahSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.144", text = "गेहे कः",
    hindiExplanation = "गृह (गेह) अर्थ में 'ग्रह्' धातु से 'क' प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310144,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == null &&
        context.allEffectiveTerms.none { it.upadesha == "क" }

    override fun apply(context: DerivationState): DerivationChange {
        val ka = DerivationTerm("ka", "क", TermKind.PRATYAYA, upadesha = "क", createdBySutra = sutra, itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA)
        return DerivationChange(
            state = context.addTerm(ka),
            explanation = "3.1.144 prescribes क affix for grah in house sense.",
        )
    }
}
