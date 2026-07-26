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
 * 4.1.5: ऋन्नेभ्यो ङीप्.
 * Prescribes 'ङीप्' (ṅīp -> ई) feminine suffix after 'ṛ'-ending or 'n'-ending stems.
 */
object RnnebyoNipSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.1.5",
    text = "ऋन्नेभ्यो ङीप्",
    hindiExplanation = "ऋकारान्त तथा नकारान्त प्रातिपदिकों से स्त्रीत्व की विवक्षा में ङीप् प्रत्यय होता है।",
    type = SutraType.UTSARGA,
    chapter = 4,
    pada = 1,
    optional = false,
    kramaValue = 410005,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage != DerivationStage.INITIAL && context.stage != DerivationStage.PRATYAYA_SELECTED) return false
        val isNipRequested = context.samjnas.any { it.samjna == Samjna.NIP }
        val hasPratyaya = context.terms.any { it.kind == TermKind.PRATYAYA }
        return isNipRequested && !hasPratyaya
    }

    override fun apply(context: DerivationState): DerivationChange {
        val nipTerm = DerivationTerm(
            id = "nip_pratyaya",
            surface = "ई",
            kind = TermKind.PRATYAYA,
            itMarkers = setOf(ItMarker.NG, ItMarker.P),
            upadesha = "ङीप्",
            createdBySutra = sutra,
        )
        return DerivationChange(
            state = context.copy(
                terms = context.terms + nipTerm,
                stage = DerivationStage.PRATYAYA_SELECTED,
            ),
            explanation = "4.1.5 introduces feminine suffix ङीप् (ई)."
        )
    }
}
