package dev.sanskrit.ashtadhyayi.adhyaya3.pada4

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

/** 3.4.7: लिङर्थे लेट्. Selects the Vedic subjunctive in the semantic domain of LIṄ. */
object LingartheLetSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.7",
    text = "लिङर्थे लेट्",
    hindiExplanation = "छन्दस् में लिङ् के अर्थ में लेट् लकार होता है।",
    type = SutraType.NITYA,
    chapter = 3,
    pada = 4,
    optional = false,
    kramaValue = 340007,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == Lakara.LET && context.stage == DerivationStage.INITIAL

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.addTerm(DerivationTerm("let", "लेट्", TermKind.PRATYAYA, upadesha = "लेट्"))
            .copy(stage = DerivationStage.PRATYAYA_SELECTED),
        "3.4.7 selects लेट् in the semantic domain of लिङ्.",
    )
}
