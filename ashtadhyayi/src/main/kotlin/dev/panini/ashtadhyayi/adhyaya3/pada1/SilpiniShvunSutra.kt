package dev.panini.ashtadhyayi.adhyaya3.pada1

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
 * Sūtra 3.1.145 शिल्पििन ष्वुन्.
 * Prescribes ṣvun agent affix for artisan sense.
 */
object SilpiniShvunSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.145", text = "शिल्पििन ष्वुन्",
    hindiExplanation = "शिल्पी (कलाकार) अर्थ में कर्ता कारक में 'ष्वुन्' प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310145,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == null &&
        context.allEffectiveTerms.none { it.upadesha == "ष्वुन्" }

    override fun apply(context: DerivationState): DerivationChange {
        val shvun = DerivationTerm("shvun", "ष्वुन्", TermKind.PRATYAYA, upadesha = "ष्वुन्", createdBySutra = sutra, itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA)
        return DerivationChange(
            state = context.addTerm(shvun),
            explanation = "3.1.145 prescribes ष्वुन् agent affix for artisan sense.",
        )
    }
}
