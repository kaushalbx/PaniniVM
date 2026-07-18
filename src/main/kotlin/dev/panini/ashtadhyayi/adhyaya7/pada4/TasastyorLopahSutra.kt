package dev.panini.ashtadhyayi.adhyaya7.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
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
    override fun matches(context: DerivationState): Boolean {
        val tasiIndex = context.terms.indexOfFirst { it.upadesha == "तासि" || it.id == "tasi" }
        if (tasiIndex == -1 || tasiIndex == context.terms.size - 1) return false

        val tasiTerm = context.terms[tasiIndex]
        val nextTerm = context.terms[tasiIndex + 1]

        return tasiTerm.surface.endsWith("स्") &&
            (nextTerm.surface.startsWith("स") || nextTerm.surface.startsWith("र"))
    }

    override fun apply(context: DerivationState): DerivationChange {
        val tasiIndex = context.terms.indexOfFirst { it.upadesha == "तासि" || it.id == "tasi" }
        val tasiTerm = context.terms[tasiIndex]

        val newSurface = tasiTerm.surface.removeSuffix("स्")

        return DerivationChange(
            state = context.replaceTerm(tasiTerm.id, tasiTerm.copy(surface = newSurface)),
            explanation = "7.4.50: Deletes the final 's' of 'tās' before a suffix starting with '${context.terms[tasiIndex + 1].surface.first()}'."
        )
    }
}
