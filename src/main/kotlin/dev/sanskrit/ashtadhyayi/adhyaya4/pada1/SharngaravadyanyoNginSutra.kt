package dev.sanskrit.ashtadhyayi.adhyaya4.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.DerivationTerm
import dev.sanskrit.derivation.HasMorphosyntax
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.ganapatha.GanaPatha
import dev.sanskrit.shiksha.Linga
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 4.1.73: शार्ङ्गरवाद्यञो ङीन्. */
object SharngaravadyanyoNginSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.1.73", text = "शार्ङ्गरवाद्यञो ङीन्",
    hindiExplanation = "स्त्रीत्व में शार्ङ्गरवादि प्रातिपदिकों से ङीन् प्रत्यय होता है।",
    type = SutraType.APAVADA, chapter = 4, pada = 1, optional = false, kramaValue = 410073,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        HasMorphosyntax(linga = Linga.STRI).matches(context) &&
            context.terms.any { it.kind == TermKind.PRATIPADIKA && GanaPatha.isEligibleMember(51, it.surface, it.lexicalUses) } &&
            context.allEffectiveTerms.none { it.upadesha == "ङीन्" }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.addTerm(DerivationTerm("ngin-suffix", "ी", TermKind.PRATYAYA, upadesha = "ङीन्"))
            .copy(stage = DerivationStage.PRATYAYA_SELECTED),
        "4.1.73 introduces ङीन् after an eligible शार्ङ्गरवादि term in the feminine.",
    )
}
