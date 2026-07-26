package dev.panini.ashtadhyayi.adhyaya5.pada1

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
 * 5.1.119: तस्य भावस्त्वतलौ.
 * Prescribes 'त्व' (tva) and 'तल्' (tal -> ता) affixes after a nominal stem to express state/nature.
 */
object TasyaBhavasTvatalauSutra : Sutra<DerivationState, DerivationChange>(
    number = "5.1.119",
    text = "तस्य भावस्त्वतलौ",
    hindiExplanation = "षष्ठ्यन्त प्रातिपदिक से तस्य भावः (उसका भाव) अर्थ में त्व और तल् प्रत्यय होते हैं।",
    type = SutraType.UTSARGA,
    chapter = 5,
    pada = 1,
    optional = false,
    kramaValue = 510119,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage != DerivationStage.INITIAL && context.stage != DerivationStage.PRATYAYA_SELECTED) return false
        val hasPratipadika = context.terms.any { it.kind == TermKind.PRATIPADIKA }
        val isTvaRequested = context.samjnas.any { it.samjna == Samjna.TVA || it.samjna == Samjna.TAL }
        val hasPratyaya = context.terms.any { it.kind == TermKind.PRATYAYA }
        return hasPratipadika && isTvaRequested && !hasPratyaya
    }

    override fun apply(context: DerivationState): DerivationChange {
        val isTal = context.samjnas.any { it.samjna == Samjna.TAL }
        val pratyayaTerm = if (isTal) {
            DerivationTerm(
                id = "tal_pratyaya",
                surface = "ता",
                kind = TermKind.PRATYAYA,
                itMarkers = emptySet(),
                upadesha = "तल्",
                createdBySutra = sutra,
            )
        } else {
            DerivationTerm(
                id = "tva_pratyaya",
                surface = "त्व",
                kind = TermKind.PRATYAYA,
                upadesha = "त्व",
                createdBySutra = sutra,
            )
        }
        return DerivationChange(
            state = context.copy(
                terms = context.terms + pratyayaTerm,
                stage = DerivationStage.FINAL,
            ),
            explanation = "5.1.119 introduces abstract affix ${pratyayaTerm.upadesha} (${pratyayaTerm.surface})."
        )
    }
}
