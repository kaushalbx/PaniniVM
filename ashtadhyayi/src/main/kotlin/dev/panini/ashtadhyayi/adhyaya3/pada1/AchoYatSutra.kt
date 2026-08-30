package dev.panini.ashtadhyayi.adhyaya3.pada1

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
 * Sūtra 3.1.97 अचो यत्.
 * Prescribes yat kṛtya affix after vowel-ending roots.
 */
object AchoYatSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.97", text = "अचो यत्",
    hindiExplanation = "अजन्त (स्वर-अन्त) धातुओं से 'यत्' कृत्य प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310097,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == null &&
        context.effectiveContext.requestedMeaning == DerivationalMeaning.BHAVA &&
        context.allEffectiveTerms.none { it.upadesha == "यत्" }

    override fun apply(context: DerivationState): DerivationChange {
        val yat = DerivationTerm("yat", "यत्", TermKind.PRATYAYA, upadesha = "यत्", itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA)
        return DerivationChange(
            state = context.addTerm(yat),
            explanation = "3.1.97 prescribes यत् kṛtya affix after ajanta dhātu.",
        )
    }
}
