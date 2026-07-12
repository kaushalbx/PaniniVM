package dev.sanskrit.ashtadhyayi.adhyaya3.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.DerivationTerm
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
        context.terms.lastOrNull()?.upadesha in TingAffix.entries.map { it.upadesha } &&
                context.terms.none { it.upadesha == "शप्" }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        state = context.copy(
            terms = context.terms.dropLast(1) +
                    DerivationTerm(
                        "shap",
                        Svara.A.devanagari,
                        TermKind.PRATYAYA,
                        upadesha = "शप्"
                    ) +
                    context.terms.last(),
        ),
        explanation = "3.1.68 introduces शप् after the dhātu.",
    )
}
