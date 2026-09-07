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
 * Sūtra 3.1.7 धातोः कर्मणः समानकर्तृकादिच्छायां सन्.
 * Prescribes san desiderative affix after roots in desire sense.
 */
object DhatohKarmanahSamanakartrkadIcchayamSanSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.7", text = "धातोः कर्मणः समानकर्तृकादिच्छायां सन्",
    hindiExplanation = "समान कर्ता वाली इच्छा (चाहना) अर्थ में कर्मवाचक धातु से 'सन्' प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310007,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
    blocks = setOf("3.1.25", "3.1.68", "3.1.69", "3.1.73", "3.1.77", "3.1.78", "3.1.79", "3.1.81"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        ("सन्" in context.effectiveContext.requestedSanadi ||
            (context.effectiveContext.rupa.lakara == null && context.effectiveContext.requestedMeaning == DerivationalMeaning.BHAVA)) &&
        context.allEffectiveTerms.none { it.upadesha == "सन्" }

    override fun apply(context: DerivationState): DerivationChange {
        val san = DerivationTerm("san", "सन्", TermKind.PRATYAYA, upadesha = "सन्", createdBySutra = number, itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA)
        val introduced = if (context.terms.lastOrNull()?.kind == TermKind.PRATYAYA) context.insertBeforeTingOrLingAugment(san) else context.addTerm(san)
        return DerivationChange(
            state = introduced.copy(stage = context.stage),
            explanation = "3.1.7 prescribes सन् desiderative affix.",
        )
    }
}
