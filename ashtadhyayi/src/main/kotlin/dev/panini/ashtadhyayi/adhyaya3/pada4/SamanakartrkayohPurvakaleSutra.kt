package dev.panini.ashtadhyayi.adhyaya3.pada4

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
 * Sūtra 3.4.21 समानकर्तृकयोः पूर्वकाले.
 * Prescribes ktvā / lyap gerund affix when two actions share an agent and one precedes the other.
 */
object SamanakartrkayohPurvakaleSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.21", text = "समानकर्तृकयोः पूर्वकाले",
    hindiExplanation = "समान कर्ता वाली दो क्रियाओं में पूर्वकाल की क्रियावाचक धातु से 'क्त्वा' (उपसर्गयुक्त होने पर 'ल्पँ') प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 4, optional = false, kramaValue = 340021,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == null &&
        context.effectiveContext.requestedMeaning == DerivationalMeaning.BHAVA &&
        context.allEffectiveTerms.none { it.upadesha in setOf("क्त्वा", "ल्पँ") }

    override fun apply(context: DerivationState): DerivationChange {
        val hasUpasarga = context.allEffectiveTerms.any { it.id == "upasarga" || it.upadesha in setOf("अनु", "सम्", "प्र", "उप") }
        val gerundTerm = if (hasUpasarga) {
            DerivationTerm("lyap", "य", TermKind.PRATYAYA, upadesha = "ल्पँ")
        } else {
            DerivationTerm("ktva", "त्वा", TermKind.PRATYAYA, upadesha = "क्त्वा")
        }
        return DerivationChange(
            state = context.addTerm(gerundTerm),
            explanation = "3.4.21 prescribes ${gerundTerm.upadesha} gerund affix.",
        )
    }
}
