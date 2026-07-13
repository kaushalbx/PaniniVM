package dev.sanskrit.ashtadhyayi.adhyaya4.pada4

import dev.sanskrit.derivation.*
import dev.sanskrit.ganapatha.GanaPatha
import dev.sanskrit.sutra.*

/** 4.4.10: पर्पादिभ्यः ष्ठन्. */
object ParpadibhyoSthanSutra : Sutra<DerivationState, DerivationChange>(number = "4.4.10", text = "पर्पादिभ्यः ष्ठन्", hindiExplanation = "चरति के अर्थ में पर्पादि से ष्ठन् होता है।", type = SutraType.APAVADA, chapter = 4, pada = 4, optional = false, kramaValue = 440010, role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION), DerivationSutra {
    override fun matches(context: DerivationState) = HasRequestedMeaning(DerivationalMeaning.CARATI).matches(context) && context.terms.any { it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(135, it.surface, it.lexicalUses) } && context.allEffectiveTerms.none { it.upadesha == "ष्ठन्" }
    override fun apply(context: DerivationState) = DerivationChange(context.addTerm(DerivationTerm("sthan-suffix", "इक", TermKind.PRATYAYA, upadesha = "ष्ठन्")), "4.4.10 introduces ष्ठन् after an eligible पर्पादि term.")
}
