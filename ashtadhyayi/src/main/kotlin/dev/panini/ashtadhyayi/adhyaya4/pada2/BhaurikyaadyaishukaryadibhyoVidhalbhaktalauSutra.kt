package dev.panini.ashtadhyayi.adhyaya4.pada2

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.DerivationalMeaning
import dev.panini.derivation.HasRequestedMeaning
import dev.panini.derivation.TermKind
import dev.panini.ganapatha.GanaPatha
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

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
        HasRequestedMeaning(DerivationalMeaning.VISHAYA_DESE).matches(context) && context.terms.any { term ->
            term.kind == TermKind.PRATIPADIKA && selectionFor(term) != null &&
                context.terms.none { it.id == "${term.id}-4-2-54" }
        }

    override fun apply(context: DerivationState): DerivationChange {
        val terms = context.terms.flatMap { term ->
            val selection = selectionFor(term)
            if (term.kind == TermKind.PRATIPADIKA && selection != null && context.terms.none { it.id == "${term.id}-4-2-54" }) {
                listOf(term, DerivationTerm("${term.id}-4-2-54", selection.upadesha, TermKind.PRATYAYA, upadesha = selection.upadesha, createdBySutra = number, itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA))
            } else listOf(term)
        }
        return DerivationChange(context.copy(terms = terms), "4.2.54 selects विधल् or भक्तल् for the corresponding viṣaya-deśa Gaṇa.")
    }

    private fun selectionFor(term: DerivationTerm): Selection? = selections.firstOrNull {
        GanaPatha.isEligibleMember(it.ganaIndex, term.surface, term.lexicalUses)
    }
}
