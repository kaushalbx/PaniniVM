package dev.sanskrit.ashtadhyayi.adhyaya3.pada2

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.DerivationTerm
import dev.sanskrit.derivation.Lakara
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/**
 * 3.2.111: anadyatane laṅ.
 * Selects laṅ for past tense (not of today).
 */
object AnadyataneLangSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.2.111",
    text = "अनद्यतने लङ्",
    hindiExplanation = "अनद्यतन भूतकाल में लङ् होता है।",
    type = SutraType.NITYA,
    chapter = 3,
    pada = 2,
    optional = false,
    kramaValue = 320111,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == Lakara.LANG && context.stage == DerivationStage.INITIAL

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        state = context.addTerm(DerivationTerm("lang", "लङ्", TermKind.PRATYAYA))
            .copy(stage = DerivationStage.PRATYAYA_SELECTED),
        explanation = "3.2.111 selects लङ्."
    )
}
