package dev.panini.ashtadhyayi.adhyaya3.pada3

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
 * 3.3.1: उणादयो बहुलम्.
 * Prescribes Uṇādi affixes ('उण्' -> उ) after verbal roots to form nominal stems.
 */
object UnadayoBahulamSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.3.1",
    text = "उणादयो बहुलम्",
    hindiExplanation = "धातुओं से उणादि प्रत्यय बहुल रूप से होते हैं।",
    type = SutraType.UTSARGA,
    chapter = 3,
    pada = 3,
    optional = false,
    kramaValue = 330001,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage != DerivationStage.INITIAL && context.stage != DerivationStage.PRATYAYA_SELECTED) return false
        val isUnadiRequested = context.samjnas.any { it.samjna == Samjna.UNADI }
        val hasDhatu = context.terms.any { it.kind == TermKind.DHATU }
        val hasPratyaya = context.terms.any { it.kind == TermKind.PRATYAYA }
        return isUnadiRequested && hasDhatu && !hasPratyaya
    }

    override fun apply(context: DerivationState): DerivationChange {
        val unadiTerm = DerivationTerm(
            id = "unadi_pratyaya",
            surface = "उ",
            kind = TermKind.PRATYAYA,
            itMarkers = emptySet(),
            upadesha = "उण्",
            createdBySutra = sutra,
        )
        return DerivationChange(
            state = context.copy(
                terms = context.terms + unadiTerm,
                stage = DerivationStage.PRATYAYA_SELECTED,
            ),
            explanation = "3.3.1 introduces Uṇādi affix उण् (उ)."
        )
    }
}
