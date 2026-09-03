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
 * Sūtra 3.1.5 गुप्तिज्किद्भ्यः सन्.
 * Prescribes san desiderative affix for gupt, tij, kit, etc.
 */
object GupTijKitsadbhyahSanSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.5", text = "गुप्तिज्किद्भ्यः सन्",
    hindiExplanation = "गुप्, तिज्, किद् आदि धातुओं से 'सन्' प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310005,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == null &&
        context.effectiveContext.requestedMeaning == DerivationalMeaning.BHAVA &&
        context.allEffectiveTerms.none { it.upadesha == "सन्" }

    override fun apply(context: DerivationState): DerivationChange {
        val san = DerivationTerm("san", "सन्", TermKind.PRATYAYA, upadesha = "सन्", createdBySutra = number, itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA)
        return DerivationChange(
            state = context.addTerm(san),
            explanation = "3.1.5 prescribes सन् desiderative affix for gupt/tij/kit.",
        )
    }
}
