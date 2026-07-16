package dev.sanskrit.ashtadhyayi.adhyaya3.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.DerivationTerm
import dev.sanskrit.derivation.Lakara
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.derivation.TingAffix
import dev.sanskrit.shiksha.Svara
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

object KartariShapSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.68",
    text = "कर्तरि शप्",
    hindiExplanation = "कर्तरि सार्वधातुक के पहले शप् होता है।",
    type = SutraType.NITYA,
    chapter = 3,
    pada = 1,
    optional = false,
    kramaValue = 310068,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara != Lakara.LIT &&
                context.terms.lastOrNull()?.upadesha in TingAffix.entries.map { it.upadesha } &&
                context.allEffectiveTerms.none { it.upadesha in setOf("शप्", "स्य", "तासि") }

    override fun apply(context: DerivationState): DerivationChange {
        val shap = DerivationTerm("shap", Svara.A.devanagari, TermKind.PRATYAYA, upadesha = "शप्")
        val terms = if (context.terms.any { it.id == "yasut" || it.id == "siyut" }) {
            // The liṅ augment has already been placed before tiṅ; शप् still follows the dhātu.
            listOf(context.terms.first()) + shap + context.terms.drop(1)
        } else {
            context.terms.dropLast(1) + shap + context.terms.last()
        }
        return DerivationChange(
        state = context.copy(terms = terms),
        explanation = "3.1.68 introduces शप् after the dhātu.",
        )
    }
}
