package dev.panini.ashtadhyayi.adhyaya7.pada2

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.derivation.hasCurrentAffix
import dev.panini.core.SupAffix
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 7.2.106: tadoḥ saḥ sau anantyayoḥ.
 * Substitutes 's' for non-final 't' of tad and etad stems before nominative singular 'su'.
 */
object TadohSahSauAnantyayohSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.106",
    text = "तदोः सः सौ अनन्त्ययोः",
    hindiExplanation = "तद् और एतद् के तकार को सकार आदेश होता है सुँ-प्रत्यय परे होने पर।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 2,
    optional = false,
    kramaValue = 720106,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    nimittaScope = NimittaScope.BOTH,
    dependencies = setOf("6.4.1"),
), DerivationSutra {
    private fun target(context: DerivationState): Pair<DerivationTerm, Int>? {
        val suffix = context.terms.lastOrNull()
        val stem = if (suffix?.sourceSupAffix == SupAffix.SU && suffix.hasCurrentAffix(SupAffix.SU))
            context.terms.getOrNull(context.terms.lastIndex - 1)
        else context.terms.firstOrNull { "7.2.107" in it.establishedBySutras }?.takeIf {
            context.droppedTerms.any { dropped -> dropped.sourceSupAffix == SupAffix.SU && dropped.droppedBySutra == "7.2.107" }
        }
        if (stem == null || stem.kind != TermKind.PRATIPADIKA || stem.upadesha !in setOf("तद्", "एतद्", "अदस्")) return null
        val index = stem.varnas.indices.firstOrNull { it < stem.varnas.lastIndex && stem.varnas[it] in setOf(Vyanjana.TA, Vyanjana.DA) } ?: return null
        return stem to index
    }

    override fun matches(context: DerivationState): Boolean = target(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val (stem, locus) = requireNotNull(target(context))

        return DerivationChange(
            state = context.replaceTermVarna(stem.id, locus, listOf(Vyanjana.SA), sutra)
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "7.2.106 replaces non-final t/d with s under the retained su condition; ṣatva is separate."
        )
    }
}
