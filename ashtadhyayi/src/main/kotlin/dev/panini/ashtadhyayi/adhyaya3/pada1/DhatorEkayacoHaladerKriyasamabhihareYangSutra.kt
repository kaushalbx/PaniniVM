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
 * Sūtra 3.1.22 धातोरेकाचो हलआदेः क्रियासमभिहारे यङ्.
 * Prescribes yaṅ frequentative affix for monosyllabic consonant-beginning roots in repetition/intensity sense.
 */
object DhatorEkayacoHaladerKriyasamabhihareYangSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.22", text = "धातोरेकाचो हलआदेः क्रियासमभिहारे यङ्",
    hindiExplanation = "क्रिया की पौनःपुन्य (बार-बार होना) या भृश (अतिशयता) अर्थ में एकाच् तथा हलादि धातु से 'यङ्' प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310022,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == null &&
        context.effectiveContext.requestedMeaning == DerivationalMeaning.BHAVA &&
        context.allEffectiveTerms.none { it.upadesha == "यङ्" }

    override fun apply(context: DerivationState): DerivationChange {
        val yang = DerivationTerm("yang", "यङ्", TermKind.PRATYAYA, upadesha = "यङ्", createdBySutra = number, itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA)
        return DerivationChange(
            state = context.addTerm(yang),
            explanation = "3.1.22 prescribes यङ् frequentative affix.",
        )
    }
}
