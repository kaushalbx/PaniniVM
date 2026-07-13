package dev.sanskrit.ashtadhyayi.adhyaya4.pada2

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

/** 4.2.60: क्रतूक्थादिसूत्रान्ताट्ठक्. */
object KratukthadisutrantatThakSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.2.60", text = "क्रतूक्थादिसूत्रान्ताट्ठक्",
    hindiExplanation = "तदधीते तद्वेद के अर्थ में उक्थादि प्रातिपदिकों से ठक् प्रत्यय होता है।",
    type = SutraType.APAVADA, chapter = 4, pada = 2, optional = false, kramaValue = 420060,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        SemanticFeature.ADHYAYANA_VEDANA in context.semanticFeatures &&
            context.terms.any { it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(79, it.surface, it.lexicalUses) } &&
            context.allEffectiveTerms.none { it.upadesha == "ठक्" }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.addTerm(DerivationTerm("thak-suffix", "इक", TermKind.PRATYAYA, upadesha = "ठक्")),
        "4.2.60 introduces ठक् after an eligible उक्थादि term in the study-or-knowledge sense.",
    )
}
