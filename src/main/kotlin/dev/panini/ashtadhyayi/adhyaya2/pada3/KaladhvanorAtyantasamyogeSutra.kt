package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.core.Karaka
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

/** Sūtra 2.3.5 कालाध्वनोरत्यन्तसंयोगे. Assigns Dvitīyā for continuous duration of time or spatial extent. */
object KaladhvanorAtyantasamyogeSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.5", text = "कालाध्वनोरत्यन्तसंयोगे",
    hindiExplanation = "अत्यन्तसंयोगे गम्यमाने कालाध्वनोर्द्वितीया स्यात्।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230005,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean {
        val hasSpecificRelation = context.participant?.semanticRelations.orEmpty().any {
            it !in setOf(SemanticRelation.DESIRED_OBJECT, SemanticRelation.INDIFFERENT_OBJECT, SemanticRelation.MOTION_GOAL)
        }
        return !context.abhihita &&
            !hasSpecificRelation &&
            (context.karaka == Karaka.KARMAN || context.karaka == Karaka.ANIRDHARITA) &&
            Vibhakti.DVITIYA in context.morphologicalCandidates
    }

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.DVITIYA,
        KarakaEvidence(number, text, "द्वितीया assigns case for continuous duration of time or spatial distance (2.3.5)."),
    )
}
