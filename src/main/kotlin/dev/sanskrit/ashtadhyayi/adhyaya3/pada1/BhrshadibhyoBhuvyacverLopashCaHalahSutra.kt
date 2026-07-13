package dev.sanskrit.ashtadhyayi.adhyaya3.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.DerivationTerm
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.ganapatha.GanaPatha
import dev.sanskrit.shiksha.SemanticFeature
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 3.1.12: भृशादिभ्यो भुव्यच्वेर्लोपश्च हलः. */
object BhrshadibhyoBhuvyacverLopashCaHalahSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.12",
    text = "भृशादिभ्यो भुव्यच्वेर्लोपश्च हलः",
    hindiExplanation = "भू होने के अर्थ में भृशादि शब्दों से, च्वि प्रत्यय के बाद नहीं, क्यङ् होता है और अन्त का हल् लुप्त होता है।",
    type = SutraType.APAVADA,
    chapter = 3,
    pada = 1,
    optional = false,
    kramaValue = 310012,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        SemanticFeature.BHAVE in context.semanticFeatures && context.terms.any { term ->
            term.kind == TermKind.PRATIPADIKA &&
                term.upadesha != "च्वि" &&
                GanaPatha.isEligibleMember(32, term.surface, term.lexicalUses) &&
                context.terms.none { it.id == "${term.id}-kyan" }
        }

    override fun apply(context: DerivationState): DerivationChange {
        val terms = context.terms.flatMap { term ->
            if (
                term.kind == TermKind.PRATIPADIKA &&
                term.upadesha != "च्वि" &&
                GanaPatha.isEligibleMember(32, term.surface, term.lexicalUses) &&
                context.terms.none { it.id == "${term.id}-kyan" }
            ) {
                listOf(
                    term.copy(surface = term.surface.dropFinalHal()),
                    DerivationTerm("${term.id}-kyan", "य", TermKind.PRATYAYA, upadesha = "क्यङ्"),
                )
            } else listOf(term)
        }
        return DerivationChange(
            state = context.copy(terms = terms),
            explanation = "3.1.12 introduces क्यङ् after eligible भृशादि terms in the becoming sense and deletes a final hal.",
        )
    }

    private fun String.dropFinalHal(): String = if (endsWith("्")) dropLast(2) else this
}
