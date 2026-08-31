package dev.panini.ashtadhyayi.adhyaya3.pada4

import dev.panini.core.PadaType
import dev.panini.core.TingAffix
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

/** 3.4.102: liṅaḥ sīyuṭ. Inserts the effective augment सीय् before an Ātmanepada liṅ termination. */
object LingasSiyutSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.102",
    text = "लिङः सीयुट्",
    hindiExplanation = "लिङ् के तिङ् प्रत्यय से पहले सीयुट् आगम होता है।",
    type = SutraType.NITYA,
    chapter = 3,
    pada = 4,
    optional = false,
    kramaValue = 340102,
    role = SutraRole.Vidhi,
    action = SutraAction.AGAMA,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val ting = context.terms.lastOrNull() ?: return false
        return context.stage != DerivationStage.INITIAL &&
            context.terms.none { it.itProcessingPending } &&
            ting.kind == TermKind.PRATYAYA &&
            ting.matchesUpadesha("लिङ्") &&
            TingAffix.entries.any { it.upadesha == ting.upadesha } &&
            TingAffix.entries.none { it.upadesha == ting.upadesha && it.pada == PadaType.PARASMAIPADA } &&
            context.allEffectiveTerms.none { it.id == "siyut" || it.id == "yasut" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ting = context.terms.last()
        val siyut = DerivationTerm("siyut", "सीयुँट्", TermKind.AGAMA, upadesha = "सीयुट्", itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA)
        return DerivationChange(
            context.copy(terms = context.terms.dropLast(1) + siyut + ting),
            "3.4.102 inserts सीयुट् before the liṅ tiṅ termination.",
        )
    }
}
