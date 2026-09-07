package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.core.Prayoga
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.HasMorphosyntax
import dev.panini.derivation.ItProcessingPhase
import dev.panini.derivation.TermKind
import dev.panini.ganapatha.GanaPatha
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

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
    private data class Selection(val ganaIndex: Int, val upadesha: String)

    private val selections = listOf(
        Selection(36, "ल्यु"),
        Selection(37, "णिनिँ"),
        Selection(38, "अच्"),
    )

    override fun matches(context: DerivationState): Boolean =
        HasMorphosyntax(prayoga = Prayoga.KARTARI).matches(context) && context.terms.any { term ->
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
                listOf(
                    term,
                    DerivationTerm(
                        id = "${term.id}-3-1-134",
                        surface = selection.upadesha,
                        kind = TermKind.PRATYAYA,
                        upadesha = selection.upadesha,
                        createdBySutra = sutra,
                        itProcessingPhase = ItProcessingPhase.RAW_UPADESHA,
                    ),
                )
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
