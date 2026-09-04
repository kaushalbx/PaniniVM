package dev.panini.ashtadhyayi.adhyaya4.pada2

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.DerivationalMeaning
import dev.panini.derivation.HasRequestedMeaning
import dev.panini.derivation.TermKind
import dev.panini.ganapatha.GanaPatha
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 4.2.53: राजन्यादिभ्यो वुञ्. */
object RajanyadibhyoVunSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.2.53", text = "राजन्यादिभ्यो वुञ्",
    hindiExplanation = "विषय-देश के अर्थ में राजन्यादि प्रातिपदिकों से वुञ् प्रत्यय होता है।",
    type = SutraType.APAVADA, chapter = 4, pada = 2, optional = false, kramaValue = 420053,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        HasRequestedMeaning(DerivationalMeaning.VISHAYA_DESE).matches(context) &&
            context.terms.any { it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(76, it.surface, it.lexicalUses) } &&
            context.allEffectiveTerms.none { it.upadesha == "वुञ्" }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.addTerm(DerivationTerm("vun-suffix", "वुञ्", TermKind.PRATYAYA, upadesha = "वुञ्", createdBySutra = number, itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA)),
        "4.2.53 introduces वुञ् after an eligible राजन्यादि term in the viṣaya-deśa sense.",
    )
}
