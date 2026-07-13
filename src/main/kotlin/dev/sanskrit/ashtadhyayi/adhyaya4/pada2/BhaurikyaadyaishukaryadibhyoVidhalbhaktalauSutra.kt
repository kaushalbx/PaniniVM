package dev.sanskrit.ashtadhyayi.adhyaya4.pada2

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

/** 4.2.54: भौरिक्याद्यैषुकार्यादिभ्यो विधल्भक्तलौ. */
object BhaurikyaadyaishukaryadibhyoVidhalbhaktalauSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.2.54", text = "भौरिक्याद्यैषुकार्यादिभ्यो विधल्भक्तलौ",
    hindiExplanation = "विषय-देश के अर्थ में भौरिक्यादि से विधल् और ऐषुकार्यादि से भक्तल् प्रत्यय होता है।",
    type = SutraType.APAVADA, chapter = 4, pada = 2, optional = false, kramaValue = 420054,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    private data class Selection(val ganaIndex: Int, val upadesha: String, val surface: String)
    private val selections = listOf(Selection(77, "विधल्", "विध"), Selection(78, "भक्तल्", "भक्त"))

    override fun matches(context: DerivationState): Boolean =
        SemanticFeature.VISHAYA_DESE in context.semanticFeatures && context.terms.any { term ->
            term.kind == TermKind.PRATIPADIKA && selectionFor(term) != null &&
                context.terms.none { it.id == "${term.id}-4-2-54" }
        }

    override fun apply(context: DerivationState): DerivationChange {
        val terms = context.terms.flatMap { term ->
            val selection = selectionFor(term)
            if (term.kind == TermKind.PRATIPADIKA && selection != null && context.terms.none { it.id == "${term.id}-4-2-54" }) {
                listOf(term, DerivationTerm("${term.id}-4-2-54", selection.surface, TermKind.PRATYAYA, upadesha = selection.upadesha))
            } else listOf(term)
        }
        return DerivationChange(context.copy(terms = terms), "4.2.54 selects विधल् or भक्तल् for the corresponding viṣaya-deśa Gaṇa.")
    }

    private fun selectionFor(term: DerivationTerm): Selection? = selections.firstOrNull {
        GanaPatha.isEligibleMember(it.ganaIndex, term.surface, term.lexicalUses)
    }
}
