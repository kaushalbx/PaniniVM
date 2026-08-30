package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 3.1.7: धातोः कर्मणः समानकर्तृकादिच्छायां वा.
 * Introduces the desiderative suffix 'सन्' (san) after a verbal root for expressing desire of action.
 */
object DhatohKarmanahSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.7",
    text = "धातोः कर्मणः समानकर्तृकादिच्छायां वा",
    hindiExplanation = "समान कर्ता वाली इच्छा के विषयभूत कर्मवाचक धातु से इच्छा अर्थ में विकल्प से सन् प्रत्यय होता है।",
    type = SutraType.UTSARGA,
    chapter = 3,
    pada = 1,
    optional = false,
    kramaValue = 310007,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage != DerivationStage.INITIAL && context.stage != DerivationStage.PRATYAYA_SELECTED) return false
        val isSanRequested = context.samjnas.any { it.samjna == Samjna.SAN } || context.context.requestedMeaning == dev.panini.derivation.DerivationalMeaning.BHAVA
        val hasPratyaya = context.terms.any { it.kind == TermKind.PRATYAYA }
        return isSanRequested && !hasPratyaya
    }

    override fun apply(context: DerivationState): DerivationChange {
        val sanTerm = DerivationTerm(
            id = "san_pratyaya",
            surface = "सन्",
            kind = TermKind.PRATYAYA,
            upadesha = "सन्",
            createdBySutra = sutra,
            itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA,
        )
        return DerivationChange(
            state = context.copy(
                terms = context.terms + sanTerm,
                stage = DerivationStage.PRATYAYA_SELECTED,
            ),
            explanation = "3.1.7 introduces desiderative suffix सन् (स)."
        )
    }
}
