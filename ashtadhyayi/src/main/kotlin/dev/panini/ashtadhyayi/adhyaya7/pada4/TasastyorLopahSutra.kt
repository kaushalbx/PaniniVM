package dev.panini.ashtadhyayi.adhyaya7.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 7.4.50: tās-astyor lopaḥ.
 * The 's' of the future marker 'tās' and the root 'as' is deleted when followed by a suffix starting with 's' or 'r'.
 */
object TasastyorLopahSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.4.50",
    text = "तासस्त्योर्लोपः",
    hindiExplanation = "सकार या रेफ आदि वाले प्रत्यय परे होने पर तासि और अस्ति के सकार का लोप होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 4,
    optional = false,
    kramaValue = 740050,
    role = SutraRole.Vidhi,
    action = SutraAction.LOPA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    /** असि: the root अस् before सिप्. It may already have lost its अ. */
    private fun asRoot(context: DerivationState): Int = context.terms.indexOfFirst {
        it.kind == dev.panini.derivation.TermKind.DHATU && it.upadesha == "असँ" && it.varnas.lastOrNull() == Vyanjana.SA
    }.takeIf { it >= 0 && it < context.terms.lastIndex &&
        context.terms[it + 1].varnas.firstOrNull() in setOf(Vyanjana.SA, Vyanjana.RA) } ?: -1

    override fun matches(context: DerivationState): Boolean {
        if (asRoot(context) >= 0) return true
        val tasiIndex = context.terms.indexOfFirst { it.upadesha == "तासि" || it.id == "tasi" }
        if (tasiIndex == -1 || tasiIndex == context.terms.size - 1) return false

        val tasiTerm = context.terms[tasiIndex]
        val nextTerm = context.terms[tasiIndex + 1]

        return tasiTerm.varnas.lastOrNull() == Vyanjana.SA &&
            nextTerm.varnas.firstOrNull() in setOf(Vyanjana.SA, Vyanjana.RA)
    }

    override fun apply(context: DerivationState): DerivationChange {
        asRoot(context).takeIf { it >= 0 }?.let { index ->
            val root = context.terms[index]
            return DerivationChange(context.deleteTermVarnas(root.id, root.varnas.lastIndex, 1, sutra), "7.4.50 deletes the s of अस् before an s-initial ending.")
        }
        val tasiIndex = context.terms.indexOfFirst { it.upadesha == "तासि" || it.id == "tasi" }
        val tasiTerm = context.terms[tasiIndex]

        return DerivationChange(
            state = context.deleteTermVarnas(tasiTerm.id, tasiTerm.varnas.lastIndex, 1, sutra),
            explanation = "7.4.50 deletes the final 's' of 'tās' before an s/r-initial suffix."
        )
    }
}
