package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationalMeaning
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.HasRequestedMeaning
import dev.panini.derivation.TermKind
import dev.panini.ganapatha.GanaPatha
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 3.1.18: सुखादिभ्यः कर्तृवेदनायाम्. */
object SukhadibhyoKartrvedanayamSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.18",
    text = "सुखादिभ्यः कर्तृवेदनायाम्",
    hindiExplanation = "कर्तृ के अनुभव के अर्थ में सुखादि शब्दों से क्यङ् प्रत्यय होता है।",
    type = SutraType.APAVADA,
    chapter = 3,
    pada = 1,
    optional = false,
    kramaValue = 310018,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        HasRequestedMeaning(DerivationalMeaning.KARTR_VEDANA).matches(context) && context.terms.any { term ->
            term.kind == TermKind.PRATIPADIKA &&
                GanaPatha.isEligibleMember(34, term.surface, term.lexicalUses) &&
                context.terms.none { it.id == "${term.id}-kyan" }
        }

    override fun apply(context: DerivationState): DerivationChange {
        val terms = context.terms.flatMap { term ->
            if (
                term.kind == TermKind.PRATIPADIKA &&
                GanaPatha.isEligibleMember(34, term.surface, term.lexicalUses) &&
                context.terms.none { it.id == "${term.id}-kyan" }
            ) {
                listOf(term, DerivationTerm("${term.id}-kyan", "य", TermKind.PRATYAYA, upadesha = "क्यङ्"))
            } else listOf(term)
        }
        return DerivationChange(
            state = context.copy(terms = terms),
            explanation = "3.1.18 introduces क्यङ् after eligible सुखादि terms in the kartṛ-vedanā sense.",
        )
    }
}
