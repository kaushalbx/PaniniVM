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

/** 4.2.77: सुवास्त्वादिभ्योऽण्. */
object SuvastvadibhyoAnSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.2.77", text = "सुवास्त्वादिभ्योऽण्",
    hindiExplanation = "तस्य निवास के अर्थ में सुवास्त्वादि प्रातिपदिकों से अण् प्रत्यय होता है।",
    type = SutraType.APAVADA, chapter = 4, pada = 2, optional = false, kramaValue = 420077,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        SemanticFeature.NIVASA in context.semanticFeatures &&
            context.terms.any { it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(83, it.surface, it.lexicalUses) } &&
            context.allEffectiveTerms.none { it.upadesha == "अण्" }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.addTerm(DerivationTerm("an-suffix", "अ", TermKind.PRATYAYA, upadesha = "अण्")),
        "4.2.77 introduces अण् after an eligible सुवास्त्वादि term in the residence sense.",
    )
}
