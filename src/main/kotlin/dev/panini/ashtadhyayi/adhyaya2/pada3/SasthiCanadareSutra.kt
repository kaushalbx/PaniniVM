package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.core.Vibhakti
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.vyakaranam.analysis.KarakaEvidence
import dev.panini.vyakaranam.analysis.VibhaktiRuleContext
import dev.panini.vyakaranam.analysis.VibhaktiRuleResult

import dev.panini.vyakaranam.analysis.SemanticRelation

/** Sūtra 2.3.38 षष्ठी चानादरे. Assigns Ṣaṣṭhī or Saptamī in expressions of disregard/contempt. */
object SasthiCanadareSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.38", text = "षष्ठी चानादरे",
    hindiExplanation = "अनादराधिके भावलक्षणे षष्ठीसप्तम्यौ स्तः।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = true, kramaValue = 230038,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.KARAKA, SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            SemanticRelation.DISREGARD_ANADARA in context.participant?.semanticRelations.orEmpty() &&
            (Vibhakti.SASTHI in context.morphologicalCandidates || Vibhakti.SAPTAMI in context.morphologicalCandidates)

    override fun apply(context: VibhaktiRuleContext): VibhaktiRuleResult {
        val caseAssigned = if (Vibhakti.SASTHI in context.morphologicalCandidates) Vibhakti.SASTHI else Vibhakti.SAPTAMI
        return VibhaktiRuleResult.Assigned(
            caseAssigned,
            KarakaEvidence(number, text, "$caseAssigned expresses disregard or contempt during action (2.3.38 षष्ठी चानादरे)."),
        )
    }
}
