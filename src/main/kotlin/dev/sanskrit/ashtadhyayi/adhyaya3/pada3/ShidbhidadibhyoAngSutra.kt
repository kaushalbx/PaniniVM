package dev.sanskrit.ashtadhyayi.adhyaya3.pada3

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.DerivationTerm
import dev.sanskrit.derivation.HasMorphosyntax
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.ganapatha.GanaPatha
import dev.sanskrit.shiksha.Linga
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 3.3.104: षिद्भिदादिभ्योऽङ्. */
object ShidbhidadibhyoAngSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.3.104", text = "षिद्भिदादिभ्योऽङ्",
    hindiExplanation = "स्त्रीत्व में षिद् और भिदादि धातुओं से अङ् प्रत्यय होता है।",
    type = SutraType.APAVADA, chapter = 3, pada = 3, optional = false, kramaValue = 330104,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        HasMorphosyntax(linga = Linga.STRI).matches(context) && context.terms.any { term ->
            term.kind == TermKind.PRATIPADIKA &&
                GanaPatha.isEligibleMember(42, term.surface, term.lexicalUses) &&
                context.terms.none { it.id == "${term.id}-ang" }
        }

    override fun apply(context: DerivationState): DerivationChange {
        val terms = context.terms.flatMap { term ->
            if (
                term.kind == TermKind.PRATIPADIKA &&
                GanaPatha.isEligibleMember(42, term.surface, term.lexicalUses) &&
                context.terms.none { it.id == "${term.id}-ang" }
            ) {
                listOf(term, DerivationTerm("${term.id}-ang", "आ", TermKind.PRATYAYA, upadesha = "अङ्"))
            } else listOf(term)
        }
        return DerivationChange(context.copy(terms = terms), "3.3.104 introduces अङ् after an eligible भिदादि stem in the feminine.")
    }
}
