package dev.panini.ashtadhyayi.adhyaya4.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationalMeaning
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.HasRequestedMeaning
import dev.panini.derivation.TermKind
import dev.panini.ganapatha.GanaPatha
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 4.1.104: अनृष्यानन्तर्ये बिदादिभ्योऽञ्. */
object AnrshyanantaryeBidadibhyoAnySutra : Sutra<DerivationState, DerivationChange>(
    number = "4.1.104", text = "अनृष्यानन्तर्ये बिदादिभ्योऽञ्",
    hindiExplanation = "गोत्र अथवा अनृषि के अनन्तर अपत्य के अर्थ में बिदादि से अञ् प्रत्यय होता है।",
    type = SutraType.APAVADA, chapter = 4, pada = 1, optional = false, kramaValue = 410104,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        (HasRequestedMeaning(DerivationalMeaning.GOTRA).matches(context) ||
            HasRequestedMeaning(DerivationalMeaning.ANANTARA_APATYA).matches(context)) &&
            context.terms.any { it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(58, it.surface, it.lexicalUses) } &&
            context.allEffectiveTerms.none { it.upadesha == "अञ्" }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.addTerm(DerivationTerm("any-suffix", "अ", TermKind.PRATYAYA, upadesha = "अञ्"))
            .copy(stage = DerivationStage.PRATYAYA_SELECTED),
        "4.1.104 introduces अञ् after an eligible बिदादि term.",
    )
}
