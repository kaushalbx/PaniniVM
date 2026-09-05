package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 7.3.104: osi ca.
 * Substitutes 'e' for the final 'a' of an aṅga before the dual affix 'os'.
 */
object OsiCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.104",
    text = "ओसि च",
    hindiExplanation = "ओस् परे होने पर अकारान्त अङ्ग के अन्त्य अकार का एकार होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730104,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    dependencies = setOf("6.4.1")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage == DerivationStage.INITIAL || context.stage == DerivationStage.PRATYAYA_SELECTED) return false
        if ("6.4.1" !in context.activeAdhikaras) return false
        if (context.terms.size < 2) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        return dev.panini.shiksha.Varnamala.endsWithA(stem.surface) &&
            affix.upadesha == "ओस्" &&
            affix.surface == "ओस्"
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val oldChar = stem.surface.last()
        val newSurface = if (oldChar !in dev.panini.shiksha.Varnamala.independentVowelsOrMarks) {
            stem.surface + "े"
        } else {
            stem.surface.dropLast(1) + "े"
        }

        return DerivationChange(
            state = context.substituteTermSurface(stem.id, newSurface, oldChar, "े", sutra)
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "7.3.104: Substituted 'e' for final 'a' before 'os'."
        )
    }
}
