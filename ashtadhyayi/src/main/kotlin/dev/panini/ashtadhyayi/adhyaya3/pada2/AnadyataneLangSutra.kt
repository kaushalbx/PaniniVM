package dev.panini.ashtadhyayi.adhyaya3.pada2

import dev.panini.core.Lakara
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
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
