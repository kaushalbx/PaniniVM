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

/** 3.2.110: भूते. Selects लुङ् for a general past event. */
object BhuteLungSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.2.110",
    text = "लुङ्",
    hindiExplanation = "सामान्य भूतकाल के अर्थ में लुङ् लकार होता है।",
    type = SutraType.NITYA,
    chapter = 3,
    pada = 2,
    optional = false,
    kramaValue = 320110,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == Lakara.LUNG &&
            context.stage == DerivationStage.INITIAL

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.addTerm(DerivationTerm("lung", "लुङ्", TermKind.PRATYAYA, upadesha = "लुङ्"))
            .copy(stage = DerivationStage.PRATYAYA_SELECTED),
        "3.2.110 selects लुङ् for the requested general past.",
    )
}
