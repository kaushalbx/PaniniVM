package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.core.Vibhakti
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.analysis.KarakaEvidence
import dev.panini.analysis.SemanticRelation
import dev.panini.analysis.VibhaktiRuleContext
import dev.panini.analysis.VibhaktiRuleResult

/**
 * 2.3.44: prasitotsukābhyāṁ tṛtīyā ca.
 * Assigns instrumental (Tṛtīyā) or locative (Saptamī) with prasita (engrossed) or utsuka (eager).
 */
object PrasitotsukabhyamTrtiyaCaSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.44",
    text = "प्रसितोत्सुकाभ्यां तृतीया च",
    hindiExplanation = "प्रसित (संलग्न) और उत्सुक शब्दों के योग में तृतीया तथा सप्तमी विभक्ति होती है।",
    type = SutraType.VIBHASHA,
    chapter = 2,
    pada = 3,
    optional = true,
    kramaValue = 230044,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.KARAKA)
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        (Vibhakti.TRTIYA in context.morphologicalCandidates || Vibhakti.SAPTAMI in context.morphologicalCandidates) &&
            context.participant?.semanticRelations?.contains(SemanticRelation.ENGROSSED_ATTACHMENT) == true

    override fun apply(context: VibhaktiRuleContext): VibhaktiRuleResult {
        val assigned = if (Vibhakti.TRTIYA in context.morphologicalCandidates) Vibhakti.TRTIYA else Vibhakti.SAPTAMI
        return VibhaktiRuleResult.Assigned(
            assigned,
            KarakaEvidence(number, text, "Assigns $assigned with prasita/utsuka.")
        )
    }
}
