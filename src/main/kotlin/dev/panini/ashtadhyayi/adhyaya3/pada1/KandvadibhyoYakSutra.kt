package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.ganapatha.GanaPatha
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 3.1.27: कण्ड्वादिभ्यो यक्. */
object KandvadibhyoYakSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.27",
    text = "कण्ड्वादिभ्यो यक्",
    hindiExplanation = "कण्ड्वादि गण के प्रातिपदिकों के बाद यक् प्रत्यय होता है।",
    type = SutraType.UTSARGA,
    chapter = 3,
    pada = 1,
    optional = false,
    kramaValue = 310027,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.terms.any { term ->
            term.kind == TermKind.PRATIPADIKA &&
                GanaPatha.isEligibleMember(35, term.surface, term.lexicalUses) &&
                context.terms.none { it.id == "${term.id}-yak" }
        }

    override fun apply(context: DerivationState): DerivationChange {
        val terms = context.terms.flatMap { term ->
            if (
                term.kind == TermKind.PRATIPADIKA &&
                GanaPatha.isEligibleMember(35, term.surface, term.lexicalUses) &&
                context.terms.none { it.id == "${term.id}-yak" }
            ) {
                listOf(term, DerivationTerm("${term.id}-yak", "य", TermKind.PRATYAYA, upadesha = "यक्"))
            } else listOf(term)
        }
        return DerivationChange(
            state = context.copy(terms = terms),
            explanation = "3.1.27 introduces यक् after eligible कण्ड्वादि terms.",
        )
    }
}
