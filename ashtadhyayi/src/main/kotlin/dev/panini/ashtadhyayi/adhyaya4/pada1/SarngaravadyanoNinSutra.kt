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
 * 4.1.65: शार्ङ्गरवाद्यञो ङीन.
 * Prescribes 'ङीन' (ṅīn -> ई) feminine suffix after śārṅgaravādi class stems (e.g. nṛ -> nārī).
 */
object SarngaravadyanoNinSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.1.65",
    text = "शार्ङ्गरवाद्यञो ङीन",
    hindiExplanation = "शार्ङ्गरवादि गण के प्रातिपदिकों से स्त्रीत्व की विवक्षा में ङीन प्रत्यय होता है।",
    type = SutraType.UTSARGA,
    chapter = 4,
    pada = 1,
    optional = false,
    kramaValue = 410065,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage != DerivationStage.INITIAL && context.stage != DerivationStage.PRATYAYA_SELECTED) return false
        val isNinRequested = context.samjnas.any { it.samjna == Samjna.NIN }
        val hasPratyaya = context.terms.any { it.kind == TermKind.PRATYAYA }
        return isNinRequested && !hasPratyaya
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ninTerm = DerivationTerm(
            id = "nin_pratyaya",
            surface = "ई",
            kind = TermKind.PRATYAYA,
            itMarkers = setOf(ItMarker.NG, ItMarker.NIT),
            upadesha = "ङीन",
            createdBySutra = sutra,
        )
        return DerivationChange(
            state = context.copy(
                terms = context.terms + ninTerm,
                stage = DerivationStage.PRATYAYA_SELECTED,
            ),
            explanation = "4.1.65 introduces feminine suffix ङीन (ई)."
        )
    }
}
