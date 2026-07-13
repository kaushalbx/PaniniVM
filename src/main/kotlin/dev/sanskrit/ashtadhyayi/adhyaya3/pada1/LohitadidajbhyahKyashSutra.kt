package dev.sanskrit.ashtadhyayi.adhyaya3.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationalMeaning
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.DerivationTerm
import dev.sanskrit.derivation.HasRequestedMeaning
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.ganapatha.GanaPatha
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 3.1.13: लोहितादिडाज्भ्यः क्यष्. */
object LohitadidajbhyahKyashSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.13", text = "लोहितादिडाज्भ्यः क्यष्",
    hindiExplanation = "भवति के अर्थ में लोहितादि शब्दों से क्यष् प्रत्यय होता है।",
    type = SutraType.APAVADA, chapter = 3, pada = 1, optional = false, kramaValue = 310013,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        HasRequestedMeaning(DerivationalMeaning.BHAVA).matches(context) &&
            context.terms.any { it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(33, it.surface, it.lexicalUses) } &&
            context.allEffectiveTerms.none { it.upadesha == "क्यष्" }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.addTerm(DerivationTerm("kyash-suffix", "य", TermKind.PRATYAYA, upadesha = "क्यष्")),
        "3.1.13 introduces क्यष् after an eligible लोहितादि term in the becoming sense.",
    )
}
