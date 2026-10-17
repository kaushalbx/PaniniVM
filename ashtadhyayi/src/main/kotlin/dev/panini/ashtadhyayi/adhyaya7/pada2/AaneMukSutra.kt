package dev.panini.ashtadhyayi.adhyaya7.pada2

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.ItProcessingPhase
import dev.panini.derivation.TermKind
import dev.panini.derivation.rawTerm
import dev.panini.core.KitAugment
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 7.2.82: आने मुक्. Kit muk belongs to the preceding aṅga's final a, not to āna. */
object AaneMukSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.82",
    text = "आने मुक्",
    hindiExplanation = "आन परे अङ्ग के अन्त्य अकार को मुक् आगम होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 2,
    optional = false,
    kramaValue = 720082,
    role = SutraRole.Vidhi,
    action = SutraAction.AGAMA,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    private fun target(context: DerivationState): DerivationTerm? {
        for ((index, term) in context.terms.withIndex()) {
            if (term.kind != TermKind.PRATYAYA || term.upadesha != "शानच्" ||
                term.createdBySutra == "3.1.83" || "3.1.83" in term.establishedBySutras ||
                term.varnas != listOf(Svara.AA, Vyanjana.NA, Svara.A) ||
                term.itProcessingPhase != ItProcessingPhase.PROCESSED) continue
            val preceding = context.terms.getOrNull(index - 1) ?: continue
            if (preceding.kind !in setOf(TermKind.DHATU, TermKind.PRATIPADIKA, TermKind.PRATYAYA) ||
                preceding.itProcessingPending || preceding.varnas.lastOrNull() != Svara.A) continue
            if (context.allEffectiveTerms.any { it.createdBySutra == sutra && it.augmentTargetId == preceding.id }) continue
            return preceding
        }
        return null
    }

    override fun matches(context: DerivationState): Boolean = target(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val preceding = requireNotNull(target(context))
        val muk = KitAugment.MUK.rawTerm("${preceding.id}-muk", sutra).copy(
            augmentTargetId = preceding.id,
            augmentTargetVarnaIndex = preceding.varnas.lastIndex,
            augmentTargetVarna = Svara.A,
            mergeIntoAugmentTarget = false,
        )
        return DerivationChange(
            state = context.addTerm(muk),
            explanation = "7.2.82 introduces kit मुक् at the preceding aṅga's final a before processed आन; u is pronunciation-only.",
        )
    }
}
