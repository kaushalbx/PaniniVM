package dev.panini.ashtadhyayi.adhyaya3.pada4

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
