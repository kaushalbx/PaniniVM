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
 * Sūtra 2.3.40 आयुक्तकुशलाभ्यां चासेवायाम्.
 * Assigns Ṣaṣṭhī or Saptamī with āyukta or kuśala when expressing engrossed activity/service.
 */
object AyuktaKusalabhyamCaSevayamSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.40", text = "आयुक्तकुशलाभ्यां चासेवायाम्",
    hindiExplanation = "आयुक्त तथा कुशल शब्द के योग में तत्परता अर्थ होने पर षष्ठी अथवा सप्तमी विभक्ति होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = true, kramaValue = 230040,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            SemanticRelation.ENGROSSED_ATTACHMENT in context.participant?.semanticRelations.orEmpty() &&
            (Vibhakti.SASTHI in context.morphologicalCandidates || Vibhakti.SAPTAMI in context.morphologicalCandidates)

    override fun apply(context: VibhaktiRuleContext): VibhaktiRuleResult {
        val v = if (Vibhakti.SASTHI in context.morphologicalCandidates) Vibhakti.SASTHI else Vibhakti.SAPTAMI
        return VibhaktiRuleResult.Assigned(
            v,
            KarakaEvidence(number, text, "$v realizes attachment/engrossment with āyukta/kuśala (2.3.40)."),
        )
    }
}
