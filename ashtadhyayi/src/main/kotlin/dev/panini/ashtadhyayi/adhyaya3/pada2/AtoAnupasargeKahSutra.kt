package dev.panini.ashtadhyayi.adhyaya3.pada2

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
 * Sūtra 3.2.3 आतोऽनुपसर्गे कः.
 * Prescribes ka affix for a-ending roots when no upasarga is present.
 */
object AtoAnupasargeKahSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.2.3", text = "आतोऽनुपसर्गे कः",
    hindiExplanation = "उपसर्ग-रहित आकारान्त धातु से 'क' प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 2, optional = false, kramaValue = 320003,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == null &&
        context.allEffectiveTerms.none { it.upadesha == "क" }

    override fun apply(context: DerivationState): DerivationChange {
        val ka = DerivationTerm("ka", "क", TermKind.PRATYAYA, upadesha = "क", createdBySutra = sutra, itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA)
        return DerivationChange(
            state = context.addTerm(ka),
            explanation = "3.2.3 prescribes क affix after anā-upasarga ā-ending root.",
        )
    }
}
