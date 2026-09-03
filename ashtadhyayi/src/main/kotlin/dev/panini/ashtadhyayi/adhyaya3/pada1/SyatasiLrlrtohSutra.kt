package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

object SyatasiLrlrtohSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.1.33",
    text = "स्यतासी लृलुटोः",
    hindiExplanation = "लृ (लृट् और लृङ्) और लुट् परे होने पर धातु से परे क्रमशः स्य और तासि प्रत्यय होते हैं।",
    type = SutraType.NITYA,
    chapter = 3,
    pada = 1,
    optional = false,
    kramaValue = 310033,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    blocks = setOf("3.1.68", "6.1.77", "6.1.101"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val lastTerm = context.terms.lastOrNull() ?: return false
        val isLrlr = lastTerm.matchesUpadesha("लृट्") || lastTerm.matchesUpadesha("लृङ्")
        val isLut = lastTerm.matchesUpadesha("लुट्")

        return if (isLrlr) {
            val hasSya = context.allEffectiveTerms.any { it.upadesha == "स्य" }
            !hasSya
        } else if (isLut) {
            val hasTasi = context.allEffectiveTerms.any { it.upadesha == "तासि" }
            !hasTasi
        } else {
            false
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val lastTerm = context.terms.last()
        val isLut = lastTerm.matchesUpadesha("लुट्")
        val newTerm = if (isLut) {
            DerivationTerm(
                "tasi",
                "तासिँ",
                TermKind.PRATYAYA,
                upadesha = "तासि",
                createdBySutra = number,
                itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA,
            )
        } else {
            DerivationTerm(
                "sya",
                "स्य",
                TermKind.PRATYAYA,
                upadesha = "स्य",
                createdBySutra = number,
            )
        }
        val targetText = if (isLut) "तासि" else "स्य"
        return DerivationChange(
            state = context.copy(
                terms = context.terms.dropLast(1) + newTerm + lastTerm,
                stage = DerivationStage.PRATYAYA_SELECTED
            ),
            explanation = "3.1.33 introduces $targetText before ${lastTerm.upadesha}.",
        )
    }
}
