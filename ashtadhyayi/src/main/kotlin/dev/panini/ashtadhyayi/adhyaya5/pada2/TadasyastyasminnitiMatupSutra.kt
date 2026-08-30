package dev.panini.ashtadhyayi.adhyaya5.pada2

import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 5.2.94: तदस्यास्त्यस्मिन्निति मतुप्.
 * Prescribes 'मतुप्' (matup -> मत्) possessive affix after a nominal stem.
 */
object TadasyastyasminnitiMatupSutra : Sutra<DerivationState, DerivationChange>(
    number = "5.2.94",
    text = "तदस्यास्त्यस्मिन्निति मतुप्",
    hindiExplanation = "वह (अस्तित्वयुक्त) इसका है अथवा इसमें है इस अर्थ में प्रथमान्त सुबन्त से मतुप् प्रत्यय होता है।",
    type = SutraType.UTSARGA,
    chapter = 5,
    pada = 2,
    optional = false,
    kramaValue = 520094,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage != DerivationStage.INITIAL) return false
        val hasPratipadika = context.terms.any { it.kind == TermKind.PRATIPADIKA }
        val isMatupRequested = context.samjnas.any { it.samjna == Samjna.MATUP }
        val hasPratyaya = context.terms.any { it.kind == TermKind.PRATYAYA }
        return hasPratipadika && isMatupRequested && !hasPratyaya
    }

    override fun apply(context: DerivationState): DerivationChange {
        val matupTerm = DerivationTerm(
            id = "matup_pratyaya",
            surface = "मतुँप्",
            kind = TermKind.PRATYAYA,
            upadesha = "मतुप्",
            createdBySutra = sutra,
            itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA,
        )
        return DerivationChange(
            state = context.copy(
                terms = context.terms + matupTerm,
                stage = DerivationStage.PRATYAYA_SELECTED,
            ),
            explanation = "5.2.94 introduces possessive affix मतुप् (मत्)."
        )
    }
}
