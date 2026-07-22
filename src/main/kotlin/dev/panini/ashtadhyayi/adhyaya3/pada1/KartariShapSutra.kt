package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.core.Lakara
import dev.panini.derivation.LetFormation
import dev.panini.derivation.TermKind
import dev.panini.derivation.TingAffix
import dev.panini.shiksha.Svara
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

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
        context.effectiveContext.rupa.lakara !in setOf(Lakara.LIT, Lakara.LUNG) &&
                !(context.effectiveContext.rupa.lakara == Lakara.LET && context.effectiveContext.letFormation == LetFormation.SIP_AORIST) &&
                context.terms.lastOrNull()?.upadesha in TingAffix.entries.map { it.upadesha } &&
                context.allEffectiveTerms.none { it.upadesha in setOf("शप्", "श्यन्", "श्नु", "श", "श्नम्", "श्ना", "उ", "स्य", "तासि") }

    override fun apply(context: DerivationState): DerivationChange {
        val shap = DerivationTerm("shap", Svara.A.devanagari, TermKind.PRATYAYA, upadesha = "शप्")
        return DerivationChange(
            state = context.insertBeforeTingOrLingAugment(shap),
            explanation = "3.1.68 introduces शप् after the dhātu.",
        )
    }
}
