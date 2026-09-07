package dev.panini.ashtadhyayi.adhyaya7.pada2

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.ItProcessingPhase
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 7.2.82: आने मुक्. Adds the मुँट् augment at the beginning of processed आन. */
object AaneMukSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.82",
    text = "आने मुक्",
    hindiExplanation = "आन् प्रत्यय के आरम्भ में मुट् आगम होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 2,
    optional = false,
    kramaValue = 720082,
    role = SutraRole.Vidhi,
    action = SutraAction.AGAMA,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = context.terms.any {
        it.kind == TermKind.PRATYAYA && it.upadesha == "शानच्" &&
            it.surface == "आन" && it.itProcessingPhase == ItProcessingPhase.PROCESSED
    } && context.allEffectiveTerms.none { it.createdBySutra == sutra }

    override fun apply(context: DerivationState): DerivationChange {
        val sanac = context.terms.first { it.upadesha == "शानच्" && it.surface == "आन" }
        val muk = DerivationTerm(
            id = "${sanac.id}-muk",
            surface = "मुँट्",
            kind = TermKind.AGAMA,
            upadesha = "मुँट्",
            createdBySutra = sutra,
            itProcessingPhase = ItProcessingPhase.RAW_UPADESHA,
            augmentTargetId = sanac.id,
            mergeIntoAugmentTarget = false,
        )
        return DerivationChange(
            state = context.addTerm(muk),
            explanation = "7.2.82 introduces raw मुँट् for placement at the beginning of शानच्'s processed आन.",
        )
    }
}
