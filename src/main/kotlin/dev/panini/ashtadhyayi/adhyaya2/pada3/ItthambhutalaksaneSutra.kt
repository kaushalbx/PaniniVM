package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.core.Karaka
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

/** Sūtra 2.3.21 इत्थम्भूतलक्षणे. Assigns Trtīyā to a characteristic mark or emblem. */
object ItthambhutalaksaneSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.21", text = "इत्थम्भूतलक्षणे",
    hindiExplanation = "कञ्चित् प्रकारं प्राप्तस्य लक्षणे तृतीया स्यात्।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230021,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.KARAKA, SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            Vibhakti.TRTIYA in context.morphologicalCandidates &&
            SemanticRelation.BODY_DEFORMITY !in context.participant?.semanticRelations.orEmpty() &&
            SemanticRelation.ACCOMPANIMENT !in context.participant?.semanticRelations.orEmpty() &&
            SemanticRelation.CAUSE_HETU !in context.participant?.semanticRelations.orEmpty() &&
            SemanticRelation.ENGROSSED_ATTACHMENT !in context.participant?.semanticRelations.orEmpty() &&
            SemanticRelation.EXCLUSION_VINA !in context.participant?.semanticRelations.orEmpty() &&
            (context.karaka == Karaka.KARANA || context.karaka == Karaka.ANIRDHARITA)

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.TRTIYA,
        KarakaEvidence(number, text, "तृतीया realizes characteristic emblem or mark (2.3.21 इत्थम्भूतलक्षणे)."),
    )
}
