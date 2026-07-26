package dev.panini.ashtadhyayi.adhyaya4.pada1

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
 * 4.1.41: सिद्धगौरादिभ्यश्च.
 * Prescribes 'ङीष्' (ṅīṣ -> ई) feminine suffix after gaurādi class stems.
 */
object SiddhagauradibhyascaSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.1.41",
    text = "सिद्धगौरादिभ्यश्च",
    hindiExplanation = "गौरादि गण के प्रातिपदिकों से स्त्रीत्व की विवक्षा में ङीष् प्रत्यय होता है।",
    type = SutraType.UTSARGA,
    chapter = 4,
    pada = 1,
    optional = false,
    kramaValue = 410041,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage != DerivationStage.INITIAL && context.stage != DerivationStage.PRATYAYA_SELECTED) return false
        val isNisRequested = context.samjnas.any { it.samjna == Samjna.NIS }
        val hasPratyaya = context.terms.any { it.kind == TermKind.PRATYAYA }
        return isNisRequested && !hasPratyaya
    }

    override fun apply(context: DerivationState): DerivationChange {
        val nisTerm = DerivationTerm(
            id = "nis_pratyaya",
            surface = "ई",
            kind = TermKind.PRATYAYA,
            itMarkers = setOf(ItMarker.NG, ItMarker.SH),
            upadesha = "ङीष्",
            createdBySutra = sutra,
        )
        return DerivationChange(
            state = context.copy(
                terms = context.terms + nisTerm,
                stage = DerivationStage.PRATYAYA_SELECTED,
            ),
            explanation = "4.1.41 introduces feminine suffix ङीष् (ई)."
        )
    }
}
