package dev.panini.ashtadhyayi.adhyaya3.pada3

import dev.panini.derivation.DerivationalMeaning
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
 * Sūtra 3.3.167 कालसमयवेलासु तुमुन्.
 * Prescribes tumun infinitive affix in time expressions.
 */
object KalaSamayaVelashuTumunSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.3.167", text = "कालसमयवेलासु तुमुन्",
    hindiExplanation = "काल, समय तथा वेला उपपद रहते धातु से 'तुमुन्' प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 3, optional = false, kramaValue = 330167,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == null &&
        context.effectiveContext.requestedMeaning == DerivationalMeaning.BHAVISYAT &&
        context.allEffectiveTerms.none { it.upadesha == "तुमुँन्" }

    override fun apply(context: DerivationState): DerivationChange {
        val tumun = DerivationTerm("tumun", "तुमुँन्", TermKind.PRATYAYA, upadesha = "तुमुँन्", createdBySutra = number, itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA)
        return DerivationChange(
            state = context.addTerm(tumun),
            explanation = "3.3.167 prescribes तुमुन् infinitive affix.",
        )
    }
}
