package dev.panini.ashtadhyayi.adhyaya4.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 4.1.74: युवतिस्तिः.
 * Prescribes 'ति' (ti -> ति) feminine suffix after the 'yuvan' stem.
 */
object YuvatisTihSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.1.74",
    text = "युवतिस्तिः",
    hindiExplanation = "युवन् शब्द से स्त्रीत्व की विवक्षा में ति प्रत्यय होता है।",
    type = SutraType.UTSARGA,
    chapter = 4,
    pada = 1,
    optional = false,
    kramaValue = 410074,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    stage = dev.panini.sutra.SutraStage.PRATYAYA_SELECTION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage != DerivationStage.INITIAL && context.stage != DerivationStage.PRATYAYA_SELECTED) return false
        val isTiRequested = context.samjnas.any { it.samjna == Samjna.TI_PRATYAYA }
        val hasPratyaya = context.terms.any { it.kind == TermKind.PRATYAYA }
        return isTiRequested && !hasPratyaya
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms.first { it.kind == TermKind.PRATIPADIKA }
        val tiTerm = DerivationTerm(
            id = "ti_pratyaya",
            surface = "ति",
            kind = TermKind.PRATYAYA,
            upadesha = "ति",
            createdBySutra = sutra,
        )
        val result = if (stem.varnas.lastOrNull() == Vyanjana.NA) stem.varnas.dropLast(1) else stem.varnas
        val changed = context.substituteTermVarnas(stem.id, result, Vyanjana.NA, emptyList(), sutra)
        return DerivationChange(
            state = changed.copy(
                terms = changed.terms + tiTerm,
                stage = DerivationStage.PRATYAYA_SELECTED,
            ),
            explanation = "4.1.74 introduces feminine suffix ति (ति)."
        )
    }
}
