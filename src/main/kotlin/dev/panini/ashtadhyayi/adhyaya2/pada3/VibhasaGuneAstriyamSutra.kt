package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.core.Vibhakti
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.vyakaranam.analysis.KarakaEvidence
import dev.panini.vyakaranam.analysis.SemanticRelation
import dev.panini.vyakaranam.analysis.VibhaktiRuleContext
import dev.panini.vyakaranam.analysis.VibhaktiRuleResult

/**
 * Sūtra 2.3.25 विभाषा गुणेऽस्त्रियाम्.
 * Option for Pañcamī or Tṛtīyā for non-feminine quality expressing cause.
 */
object VibhasaGuneAstriyamSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.25", text = "विभाषा गुणेऽस्त्रियाम्",
    hindiExplanation = "अस्त्रीलिङ्ग गुणवाचक शब्द से हेतु अर्थ में पञ्चमी अथवा तृतीया विभक्ति होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = true, kramaValue = 230025,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            SemanticRelation.NON_FEMININE_QUALITY_CAUSE in context.participant?.semanticRelations.orEmpty() &&
            (Vibhakti.PANCHAMI in context.morphologicalCandidates || Vibhakti.TRTIYA in context.morphologicalCandidates)

    override fun apply(context: VibhaktiRuleContext): VibhaktiRuleResult {
        val v = if (Vibhakti.PANCHAMI in context.morphologicalCandidates) Vibhakti.PANCHAMI else Vibhakti.TRTIYA
        return VibhaktiRuleResult.Assigned(
            v,
            KarakaEvidence(number, text, "$v realizes non-feminine quality cause (2.3.25)."),
        )
    }
}
