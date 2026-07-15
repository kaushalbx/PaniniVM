package dev.sanskrit.ashtadhyayi.adhyaya3.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.DerivationTerm
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

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
    blocks = setOf("3.1.68", "6.1.77"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val lastTerm = context.terms.lastOrNull() ?: return false
        val isLrt = lastTerm.matchesUpadesha("लृट्")
        val hasSya = context.allEffectiveTerms.any { it.upadesha == "स्य" }
        return isLrt && !hasSya
    }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        state = context.copy(
            terms = context.terms.dropLast(1) +
                    DerivationTerm(
                        "sya",
                        "स्य",
                        TermKind.PRATYAYA,
                        upadesha = "स्य"
                    ) +
                    context.terms.last(),
            stage = DerivationStage.PRATYAYA_SELECTED
        ),
        explanation = "3.1.33 introduces स्य before लृट्.",
    )
}
