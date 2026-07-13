package dev.sanskrit.ashtadhyayi.adhyaya4.pada3

import dev.sanskrit.derivation.*
import dev.sanskrit.ganapatha.GanaPatha
import dev.sanskrit.sutra.*

/** 4.3.73: अणृगयनादिभ्यः. */
object RgayandibhyoAnSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.3.73", text = "अणृगयनादिभ्यः",
    hindiExplanation = "भव या व्याख्यान के अर्थ में ऋगयनादि प्रातिपदिकों से अण् प्रत्यय होता है।",
    type = SutraType.APAVADA, chapter = 4, pada = 3, optional = false, kramaValue = 430073,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState) =
        (HasRequestedMeaning(DerivationalMeaning.TATRA_BHAVA).matches(context) || HasRequestedMeaning(DerivationalMeaning.VYAKHYANA).matches(context)) &&
            context.terms.any { it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(116, it.surface, it.lexicalUses) } &&
            context.allEffectiveTerms.none { it.upadesha == "अण्" }
    override fun apply(context: DerivationState) = DerivationChange(context.addTerm(DerivationTerm("an-suffix", "अ", TermKind.PRATYAYA, upadesha = "अण्")), "4.3.73 introduces अण् after an eligible ऋगयनादि term.")
}
