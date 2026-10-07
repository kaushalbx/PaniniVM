package dev.panini.ashtadhyayi.adhyaya4.pada1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.derivation.*
import dev.panini.shiksha.Samjna
import dev.panini.sutra.*

/** 4.1.6: feminine ङीप् after an उगित् stem; eligibility is not inferred from spelling. */
object UgitashCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.1.6", text = "उगितश्च",
    hindiExplanation = "उगित् प्रातिपदिक से स्त्रीत्व में ङीप् प्रत्यय होता है।",
    type = SutraType.UTSARGA, chapter = 4, pada = 1, optional = false,
    kramaValue = 410006, role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
    stage = SutraStage.PRATYAYA_SELECTION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage != DerivationStage.INITIAL && context.stage != DerivationStage.PRATYAYA_SELECTED) return false
        if (context.effectiveContext.rupa.linga != Linga.STRI) return false
        val stem = context.terms.firstOrNull { it.kind == TermKind.PRATIPADIKA } ?: return false
        return ItMarker.U in stem.itMarkers &&
            context.samjnas.any { it.targetId == stem.id && it.samjna == Samjna.NIP } &&
            context.terms.none { it.kind == TermKind.PRATYAYA }
    }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        state = context.copy(
            terms = context.terms + DerivationTerm(
                id = "nip_pratyaya", surface = "ङीप्", kind = TermKind.PRATYAYA,
                upadesha = "ङीप्", createdBySutra = sutra,
                itProcessingPhase = ItProcessingPhase.RAW_UPADESHA,
            ),
            stage = DerivationStage.PRATYAYA_SELECTED,
        ),
        explanation = "4.1.6 introduces ङीप् after an उगित् stem.",
    )
}
