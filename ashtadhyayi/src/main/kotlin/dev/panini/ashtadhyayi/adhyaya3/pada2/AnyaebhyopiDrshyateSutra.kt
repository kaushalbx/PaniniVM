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
 * Sūtra 3.2.178 अन्येभ्योऽपि दृश्यते.
 * Prescribes kvip affix diversely for other roots.
 */
object AnyaebhyopiDrshyateSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.2.178", text = "अन्येभ्योऽपि दृश्यते",
    hindiExplanation = "अन्य धातुओं से भी 'क्विप्' आदि प्रत्यय देखे जाते हैं।",
    type = SutraType.NITYA, chapter = 3, pada = 2, optional = false, kramaValue = 320178,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == null &&
        context.allEffectiveTerms.none { it.upadesha == "क्विप्" }

    override fun apply(context: DerivationState): DerivationChange {
        val kvip = DerivationTerm("kvip", "क्विप्", TermKind.PRATYAYA, upadesha = "क्विप्", createdBySutra = sutra, itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA)
        return DerivationChange(
            state = context.addTerm(kvip),
            explanation = "3.2.178 prescribes क्विप् zero-affix diversely.",
        )
    }
}
