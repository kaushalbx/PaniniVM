package dev.panini.ashtadhyayi.adhyaya4.pada1

import dev.panini.core.Linga
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.DerivationalMeaning
import dev.panini.derivation.HasMorphosyntax
import dev.panini.derivation.HasRequestedMeaning
import dev.panini.derivation.TermKind
import dev.panini.ganapatha.GanaPatha
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 4.1.80: क्रौड्यादिभ्यश्च. */
object KraudyadibhyashCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.1.80", text = "क्रौड्यादिभ्यश्च",
    hindiExplanation = "गोत्र और स्त्रीत्व के अर्थ में क्रौड्यादि प्रातिपदिकों से ष्यङ् प्रत्यय होता है।",
    type = SutraType.APAVADA, chapter = 4, pada = 1, optional = false, kramaValue = 410080,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        HasMorphosyntax(linga = Linga.STRI).matches(context) &&
            HasRequestedMeaning(DerivationalMeaning.GOTRA).matches(context) &&
            context.terms.any { it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(52, it.surface, it.lexicalUses) } &&
            context.allEffectiveTerms.none { it.upadesha == "ष्यङ्" }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.addTerm(DerivationTerm("shyang-suffix", "य", TermKind.PRATYAYA, upadesha = "ष्यङ्"))
            .copy(stage = DerivationStage.PRATYAYA_SELECTED),
        "4.1.80 introduces ष्यङ् after an eligible क्रौड्यादि term in feminine gotra derivation.",
    )
}
