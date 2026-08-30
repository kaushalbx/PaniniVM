package dev.panini.ashtadhyayi.adhyaya3.pada2

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
 * 3.2.107: कानच्च.
 * Prescribes 'कानच्' (kānac -> आन) perfect middle participle affix after a verbal root in Liṭ.
 */
object KanacCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.2.107",
    text = "कानच्च",
    hindiExplanation = "लिट् के स्थान पर आत्मनेपद में 'कानच्' प्रत्यय होता है।",
    type = SutraType.UTSARGA,
    chapter = 3,
    pada = 2,
    optional = false,
    kramaValue = 320107,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage != DerivationStage.INITIAL && context.stage != DerivationStage.PRATYAYA_SELECTED) return false
        val isKanacRequested = context.samjnas.any { it.samjna == Samjna.KANAC }
        val hasDhatu = context.terms.any { it.kind == TermKind.DHATU }
        val hasPratyaya = context.terms.any { it.kind == TermKind.PRATYAYA }
        return isKanacRequested && hasDhatu && !hasPratyaya
    }

    override fun apply(context: DerivationState): DerivationChange {
        val kanacTerm = DerivationTerm(
            id = "kanac_pratyaya",
            surface = "कानच्",
            kind = TermKind.PRATYAYA,
            upadesha = "कानच्",
            createdBySutra = sutra,
            itProcessingPending = true,
        )
        return DerivationChange(
            state = context.copy(
                terms = context.terms + kanacTerm,
                stage = DerivationStage.PRATYAYA_SELECTED,
            ),
            explanation = "3.2.107 introduces perfect middle participle affix कानच् (आन)."
        )
    }
}
