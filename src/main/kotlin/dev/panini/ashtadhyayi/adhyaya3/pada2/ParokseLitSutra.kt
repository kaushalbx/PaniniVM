package dev.panini.ashtadhyayi.adhyaya3.pada2

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.Lakara
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 3.2.115: परोक्षे लिट्. */
object ParokseLitSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.2.115", text = "परोक्षे लिट्",
    hindiExplanation = "परोक्ष भूतकाल में लिट् लकार होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 2, optional = false, kramaValue = 320115,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == Lakara.LIT && context.stage == DerivationStage.INITIAL

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.addTerm(DerivationTerm("lit", "लिट्", TermKind.PRATYAYA, upadesha = "लिट्"))
            .copy(stage = DerivationStage.PRATYAYA_SELECTED),
        "3.2.115 selects लिट् for parokṣa past time.",
    )
}
