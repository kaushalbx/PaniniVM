package dev.panini.ashtadhyayi.adhyaya7.pada2

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
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
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        val isTadOrEtad = stem.upadesha in setOf("तद्", "एतद्") || stem.surface in setOf("तद्", "त", "एतद्", "एत")
        return isTadOrEtad && affix.id == "sup-su" && (stem.surface.startsWith("त") || stem.surface.startsWith("एत"))
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val surface = stem.surface
        val newSurface = if (surface.startsWith("एत")) {
            "एष" + surface.drop(2)
        } else {
            "स" + surface.drop(1)
        }

        return DerivationChange(
            state = context.replaceTerm(stem.id, stem.copy(surface = newSurface))
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "7.2.106: Replaced non-final 't' with 's' before 'su'."
        )
    }
}
