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

/** 4.1.45: बह्वादिभ्यश्च. */
object BahvadibhyashCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.1.45",
    text = "बह्वादिभ्यश्च",
    hindiExplanation = "स्त्रीत्व में बह्वादि गण के प्रातिपदिकों से विकल्प से ङीष् प्रत्यय होता है।",
    type = SutraType.VIBHASHA,
    chapter = 4,
    pada = 1,
    optional = true,
    kramaValue = 410045,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    dependencies = setOf("4.1.3"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        HasMorphosyntax(linga = Linga.STRI).matches(context) &&
            "4.1.3" in context.activeAdhikaras &&
            context.terms.any { term ->
                term.kind == TermKind.PRATIPADIKA &&
                    GanaPatha.isEligibleMember(49, term.surface, term.lexicalUses)
            } &&
            context.allEffectiveTerms.none { it.upadesha == "ङीष्" }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        state = context.addTerm(
            DerivationTerm("bahvadi-ngish-suffix", "ई", TermKind.PRATYAYA, upadesha = "ङीष्"),
        ).copy(stage = DerivationStage.PRATYAYA_SELECTED),
        explanation = "4.1.45 optionally introduces ङीष् after an eligible बह्वादि term.",
    )
}
