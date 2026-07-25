package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationalMeaning
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
 * Sūtra 3.1.29 ऋतीीयङ्.
 * Prescribes kyaङ् affix for ṛtīya, etc.
 */
object RtIyIyAnehKyanSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.29", text = "ऋतीीयङ्",
    hindiExplanation = "ऋतीय धातु से 'क्यङ्' (य) प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310029,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == null &&
        context.effectiveContext.requestedMeaning == DerivationalMeaning.BHAVA &&
        context.allEffectiveTerms.any { it.upadesha == "ऋतीय" || it.surface == "ऋतीय" } &&
        context.allEffectiveTerms.none { it.upadesha == "क्यङ्" }

    override fun apply(context: DerivationState): DerivationChange {
        val kyan = DerivationTerm("kyan", "य", TermKind.PRATYAYA, upadesha = "क्यङ्")
        return DerivationChange(
            state = context.addTerm(kyan),
            explanation = "3.1.29 prescribes क्यङ् affix.",
        )
    }
}
