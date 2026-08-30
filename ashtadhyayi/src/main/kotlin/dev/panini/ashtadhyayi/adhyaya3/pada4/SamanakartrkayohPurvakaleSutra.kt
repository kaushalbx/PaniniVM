package dev.panini.ashtadhyayi.adhyaya3.pada4

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
 * 3.4.21: समानकर्तृकयोः पूर्वकाले.
 * Introduces 'क्त्वा' (or 'ल्पँ' when preceded by an upasarga) after a verbal root for a prior action sharing an agent.
 */
object SamanakartrkayohPurvakaleSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.21",
    text = "समानकर्तृकयोः पूर्वकाले",
    hindiExplanation = "समान कर्ता वाली क्रियाओं में पूर्वकाल की क्रियावाचक धातु से क्त्वा (उपसर्गयुक्त होने पर ल्पँ) प्रत्यय होता है।",
    type = SutraType.UTSARGA,
    chapter = 3,
    pada = 4,
    optional = false,
    kramaValue = 340021,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    stage = dev.panini.sutra.SutraStage.PRATYAYA_SELECTION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val isKtvaRequested = context.samjnas.any { it.samjna == Samjna.KTVA } ||
            context.context.requestedMeaning == dev.panini.derivation.DerivationalMeaning.BHAVA
        val hasPratyaya = context.terms.any { it.kind == TermKind.PRATYAYA }
        return isKtvaRequested && !hasPratyaya
    }

    override fun apply(context: DerivationState): DerivationChange {
        val hasUpasarga = context.samjnas.any { it.samjna == Samjna.UPASARGA } ||
            context.terms.any { it.id.contains("upasarga") || it.upadesha in setOf("अनु", "सम्", "प्र", "उप") }

        val gerundTerm = if (hasUpasarga) {
            DerivationTerm(
                id = "lyap_pratyaya",
                surface = "ल्यप्",
                kind = TermKind.PRATYAYA,
                upadesha = "ल्यप्",
                createdBySutra = sutra,
                itProcessingPending = true,
            )
        } else {
            DerivationTerm(
                id = "ktva_pratyaya",
                surface = "क्त्वा",
                kind = TermKind.PRATYAYA,
                upadesha = "क्त्वा",
                createdBySutra = sutra,
                itProcessingPending = true,
            )
        }

        return DerivationChange(
            state = context.copy(
                terms = context.terms + gerundTerm,
                stage = DerivationStage.PRATYAYA_SELECTED,
            ),
            explanation = "3.4.21 introduces suffix ${gerundTerm.upadesha} after the root."
        )
    }
}
