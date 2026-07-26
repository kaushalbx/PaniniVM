package dev.panini.ashtadhyayi.adhyaya5.pada3

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
 * 5.3.57: अतिशायने तमबिष्ठनौ.
 * Prescribes 'तमप्' (tamap -> तम) superlative affix among many items.
 */
object TamabisthanauSutra : Sutra<DerivationState, DerivationChange>(
    number = "5.3.57",
    text = "अतिशायने तमबिष्ठनौ",
    hindiExplanation = "अतिशय अर्थ में प्रातिपदिक से तमप् और इष्ठन् प्रत्यय होते हैं।",
    type = SutraType.UTSARGA,
    chapter = 5,
    pada = 3,
    optional = false,
    kramaValue = 530057,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage != DerivationStage.INITIAL && context.stage != DerivationStage.PRATYAYA_SELECTED) return false
        val hasPratipadika = context.terms.any { it.kind == TermKind.PRATIPADIKA }
        val isTamapRequested = context.samjnas.any { it.samjna == Samjna.TAMAP }
        val hasPratyaya = context.terms.any { it.kind == TermKind.PRATYAYA }
        return hasPratipadika && isTamapRequested && !hasPratyaya
    }

    override fun apply(context: DerivationState): DerivationChange {
        val tamapTerm = DerivationTerm(
            id = "tamap_pratyaya",
            surface = "तम",
            kind = TermKind.PRATYAYA,
            itMarkers = setOf(ItMarker.P),
            upadesha = "तमप्",
            createdBySutra = sutra,
        )
        return DerivationChange(
            state = context.copy(
                terms = context.terms + tamapTerm,
                stage = DerivationStage.FINAL,
            ),
            explanation = "5.3.57 introduces superlative affix तमप् (तम)."
        )
    }
}
