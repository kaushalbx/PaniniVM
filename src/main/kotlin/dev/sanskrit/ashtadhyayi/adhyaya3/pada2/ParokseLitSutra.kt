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
