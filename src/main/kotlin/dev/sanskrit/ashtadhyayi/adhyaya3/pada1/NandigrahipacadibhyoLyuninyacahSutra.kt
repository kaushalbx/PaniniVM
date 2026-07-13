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

/** 3.1.134: नन्दिग्रहिपचादिभ्यो ल्युणिन्यचः. */
object NandigrahipacadibhyoLyuninyacahSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.134",
    text = "नन्दिग्रहिपचादिभ्यो ल्युणिन्यचः",
    hindiExplanation = "कर्तृ अर्थ में नन्द्यादि, ग्रह्यादि और पचादि गणों से क्रमशः ल्यु, णिनि और अच् प्रत्यय होते हैं।",
    type = SutraType.APAVADA,
    chapter = 3,
    pada = 1,
    optional = false,
    kramaValue = 310134,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    private data class Selection(val ganaIndex: Int, val upadesha: String, val surface: String)

    private val selections = listOf(
        Selection(36, "ल्यु", "अन"),
        Selection(37, "णिनि", "इ"),
        Selection(38, "अच्", "अ"),
    )

    override fun matches(context: DerivationState): Boolean =
        SemanticFeature.KARTARI in context.semanticFeatures && context.terms.any { term ->
            term.kind == TermKind.PRATIPADIKA &&
                selectionFor(term) != null &&
                context.terms.none { it.id == "${term.id}-3-1-134" }
        }

    override fun apply(context: DerivationState): DerivationChange {
        val terms = context.terms.flatMap { term ->
            val selection = selectionFor(term)
            if (
                term.kind == TermKind.PRATIPADIKA && selection != null &&
                context.terms.none { it.id == "${term.id}-3-1-134" }
            ) {
                listOf(term, DerivationTerm("${term.id}-3-1-134", selection.surface, TermKind.PRATYAYA, upadesha = selection.upadesha))
            } else listOf(term)
        }
        return DerivationChange(
            state = context.copy(terms = terms),
            explanation = "3.1.134 selects ल्यु, णिनि, or अच् according to the eligible Gaṇapāṭha class.",
        )
    }

    private fun selectionFor(term: DerivationTerm): Selection? = selections.firstOrNull {
        GanaPatha.isEligibleMember(it.ganaIndex, term.surface, term.lexicalUses)
    }
}
