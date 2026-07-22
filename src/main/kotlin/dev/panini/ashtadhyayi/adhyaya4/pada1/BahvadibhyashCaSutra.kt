package dev.panini.ashtadhyayi.adhyaya4.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.HasMorphosyntax
import dev.panini.derivation.TermKind
import dev.panini.ganapatha.GanaPatha
import dev.panini.core.Linga
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

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
