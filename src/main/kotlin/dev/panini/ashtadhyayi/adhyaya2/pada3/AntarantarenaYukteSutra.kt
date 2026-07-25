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
 * Sūtra 2.3.4 अन्तरान्तरेण युक्ते.
 * Assigns Dvitīyā in connection with antarā (between) and antareṇa (without/concerning).
 */
object AntarantarenaYukteSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.4", text = "अन्तरान्तरेण युक्ते",
    hindiExplanation = "अन्तरा तथा अन्तरेण शब्द के योग में द्वितीया विभक्ति होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230004,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            SemanticRelation.BETWEEN_OR_WITHOUT in context.participant?.semanticRelations.orEmpty() &&
            Vibhakti.DVITIYA in context.morphologicalCandidates

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.DVITIYA,
        KarakaEvidence(number, text, "द्वितीया realizes relation with antarā/antareṇa (2.3.4)."),
    )
}
