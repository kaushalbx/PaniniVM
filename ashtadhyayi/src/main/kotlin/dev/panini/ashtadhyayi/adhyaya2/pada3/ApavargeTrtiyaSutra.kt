package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.analysis.KarakaEvidence
import dev.panini.analysis.SemanticRelation
import dev.panini.analysis.VibhaktiRuleContext
import dev.panini.analysis.VibhaktiRuleResult
import dev.panini.core.Vibhakti
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 2.3.6 अपवर्गे तृतीया. */
object ApavargeTrtiyaSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.6",
    text = "अपवर्गे तृतीया",
    hindiExplanation = "फलप्राप्ति की सीमा बताने वाले काल अथवा मार्ग में तृतीया होती है।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 3,
    optional = false,
    kramaValue = 230006,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            SemanticRelation.COMPLETION_LIMIT in context.participant?.semanticRelations.orEmpty() &&
            SemanticRelation.CONTINUOUS_TIME_DISTANCE_EXTENT in context.participant?.semanticRelations.orEmpty() &&
            Vibhakti.TRTIYA in context.morphologicalCandidates

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.TRTIYA,
        KarakaEvidence(number, text, "Tṛtīyā marks the time or distance at whose completion a result is attained (2.3.6)."),
    )
}
