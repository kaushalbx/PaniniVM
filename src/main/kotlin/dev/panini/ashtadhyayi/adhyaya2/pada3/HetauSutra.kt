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
import dev.panini.vyakaranam.analysis.VibhaktiRuleContext
import dev.panini.vyakaranam.analysis.VibhaktiRuleResult

import dev.panini.vyakaranam.analysis.SemanticRelation

/** Sūtra 2.3.23 हेतौ. Assigns Trtīyā to express cause, motive, or reason (hetu). */
object HetauSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.23", text = "हेतौ",
    hindiExplanation = "हेतौ द्योत्ये तृतीया स्यात्।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230023,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.KARAKA, SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            Vibhakti.TRTIYA in context.morphologicalCandidates &&
            SemanticRelation.CAUSE_HETU in context.participant?.semanticRelations.orEmpty()

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.TRTIYA,
        KarakaEvidence(number, text, "तृतीया expresses cause, motive, or reason (2.3.23 हेतौ)."),
    )
}
