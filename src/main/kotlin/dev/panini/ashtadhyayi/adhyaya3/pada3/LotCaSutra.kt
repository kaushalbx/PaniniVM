package dev.panini.ashtadhyayi.adhyaya3.pada3

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
