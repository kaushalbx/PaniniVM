package dev.panini.ashtadhyayi.adhyaya3.pada3

import dev.panini.derivation.DerivationalMeaning
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
 * Sūtra 3.3.18 भावे.
 * Prescribes ghañ / ktin affixes in Bhāve action sense.
 */
object BhaveSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.3.18", text = "भावे",
    hindiExplanation = "भाव (क्रिया सिद्धवस्था) अर्थ में धातु से 'घञ्' प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 3, optional = false, kramaValue = 330018,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
    stage = dev.panini.sutra.SutraStage.PRATYAYA_SELECTION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == null &&
        context.effectiveContext.requestedMeaning == DerivationalMeaning.BHAVA &&
        context.allEffectiveTerms.none { it.upadesha == "घञ्" }

    override fun apply(context: DerivationState): DerivationChange {
        val ghan = DerivationTerm("ghan", "घञ्", TermKind.PRATYAYA, upadesha = "घञ्", itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA)
        return DerivationChange(
            state = context.addTerm(ghan),
            explanation = "3.3.18 prescribes घञ् action affix in bhāve.",
        )
    }
}
