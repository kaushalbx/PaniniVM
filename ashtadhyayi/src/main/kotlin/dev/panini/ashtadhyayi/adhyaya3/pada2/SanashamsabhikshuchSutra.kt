package dev.panini.ashtadhyayi.adhyaya3.pada2

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.2.168 सनशंसभिक्ष उच्.
 * Prescribes uc agent affix for san-stems, śaṁs, bhikṣ roots.
 */
object SanashamsabhikshuchSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.2.168", text = "सनशंसभिक्ष उच्",
    hindiExplanation = "सन्नन्त धातुओं तथा शंस्, भिक्ष् धातुओं से 'उच्' प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 2, optional = false, kramaValue = 320168,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == null &&
        context.allEffectiveTerms.none { it.upadesha == "उच्" }

    override fun apply(context: DerivationState): DerivationChange {
        val uc = DerivationTerm("uc", "उ", TermKind.PRATYAYA, upadesha = "उच्")
        return DerivationChange(
            state = context.addTerm(uc),
            explanation = "3.2.168 prescribes उच् agent affix.",
        )
    }
}
