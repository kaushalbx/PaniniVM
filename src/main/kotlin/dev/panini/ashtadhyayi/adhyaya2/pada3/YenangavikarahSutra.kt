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

/** Sūtra 2.3.20 येनाङ्गविकारः. Assigns Trtīyā to the body part through which a physical deformity is indicated. */
object YenangavikarahSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.20", text = "येनाङ्गविकारः",
    hindiExplanation = "येनाङ्गेन अङ्गिनो विकारो लक्ष्यते ततः तृतीया स्यात्।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230020,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.KARAKA, SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            Vibhakti.TRTIYA in context.morphologicalCandidates &&
            SemanticRelation.BODY_DEFORMITY in context.participant?.semanticRelations.orEmpty()

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.TRTIYA,
        KarakaEvidence(number, text, "तृतीया characterizes body limb deformity (2.3.20)."),
    )
}
