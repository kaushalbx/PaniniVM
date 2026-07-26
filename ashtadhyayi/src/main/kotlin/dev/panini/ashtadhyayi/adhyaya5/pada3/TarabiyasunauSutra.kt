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
 * 5.3.55: द्विवचनविभज्योपपदे तरबीयसुनौ.
 * Prescribes 'तरप्' (tarap -> तर) comparative affix between two items.
 */
object TarabiyasunauSutra : Sutra<DerivationState, DerivationChange>(
    number = "5.3.55",
    text = "द्विवचनविभज्योपपदे तरबीयसुनौ",
    hindiExplanation = "दो पदार्थों के अतिशय निर्धारण में तरप् और ईयसुन् प्रत्यय होते हैं।",
    type = SutraType.UTSARGA,
    chapter = 5,
    pada = 3,
    optional = false,
    kramaValue = 530055,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage != DerivationStage.INITIAL && context.stage != DerivationStage.PRATYAYA_SELECTED) return false
        val hasPratipadika = context.terms.any { it.kind == TermKind.PRATIPADIKA }
        val isTarapRequested = context.samjnas.any { it.samjna == Samjna.TARAP }
        val hasPratyaya = context.terms.any { it.kind == TermKind.PRATYAYA }
        return hasPratipadika && isTarapRequested && !hasPratyaya
    }

    override fun apply(context: DerivationState): DerivationChange {
        val tarapTerm = DerivationTerm(
            id = "tarap_pratyaya",
            surface = "तर",
            kind = TermKind.PRATYAYA,
            itMarkers = setOf(ItMarker.P),
            upadesha = "तरप्",
            createdBySutra = sutra,
        )
        return DerivationChange(
            state = context.copy(
                terms = context.terms + tarapTerm,
                stage = DerivationStage.FINAL,
            ),
            explanation = "5.3.55 introduces comparative affix तरप् (तर)."
        )
    }
}
