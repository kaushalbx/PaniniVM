package dev.sanskrit.ashtadhyayi.adhyaya3.pada3

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

/** 3.3.162: loṭ ca. Selects the imperative lakāra. */
object LotCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.3.162",
    text = "लोट् च",
    hindiExplanation = "आज्ञा अथवा प्रार्थना के अर्थ में लोट् लकार होता है।",
    type = SutraType.NITYA,
    chapter = 3,
    pada = 3,
    optional = false,
    kramaValue = 330162,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == Lakara.LOT && context.stage == DerivationStage.INITIAL

    override fun apply(context: DerivationState) = DerivationChange(
        context.addTerm(DerivationTerm("lot", "लोट्", TermKind.PRATYAYA, upadesha = "लोट्"))
            .copy(stage = DerivationStage.PRATYAYA_SELECTED),
        "3.3.162 selects लोट्.",
    )
}
