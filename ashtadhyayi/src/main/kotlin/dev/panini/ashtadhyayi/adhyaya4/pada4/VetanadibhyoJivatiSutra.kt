package dev.panini.ashtadhyayi.adhyaya4.pada4

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

/** 4.4.12: वेतनादिभ्यो जीवति. */
object VetanadibhyoJivatiSutra : Sutra<DerivationState, DerivationChange>(number = "4.4.12", text = "वेतनादिभ्यो जीवति", hindiExplanation = "जिससे जीविका चलती है उस अर्थ में वेतनादि से ठक् होता है।", type = SutraType.APAVADA, chapter = 4, pada = 4, optional = false, kramaValue = 440012, role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION), DerivationSutra {
    override fun matches(context: DerivationState) = HasRequestedMeaning(DerivationalMeaning.JIVATI).matches(context) && context.terms.any { it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(136, it.surface, it.lexicalUses) } && context.allEffectiveTerms.none { it.upadesha == "ठक्" }
    override fun apply(context: DerivationState) = DerivationChange(context.addTerm(DerivationTerm("thak-suffix", "ठक्", TermKind.PRATYAYA, upadesha = "ठक्", createdBySutra = number, itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA)), "4.4.12 introduces ठक् after an eligible वेतनादि term.")
}
