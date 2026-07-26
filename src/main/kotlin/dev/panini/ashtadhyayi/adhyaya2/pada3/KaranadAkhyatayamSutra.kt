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
 * Sūtra 2.3.33 करणादाख्यातायाम्.
 * Assigns Pañcamī (or Tṛtīyā) for the instrument of gambling when used with an inflected verb.
 */
object KaranadAkhyatayamSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.33", text = "करणादाख्यातायाम्",
    hindiExplanation = "आख्यात (तिङन्त क्रिया) का योग होने पर द्यूत के साधन (करण) में पञ्चमी अथवा तृतीया विभक्ति होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = true, kramaValue = 230033,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            SemanticRelation.GAMBLING_INSTRUMENT in context.participant?.semanticRelations.orEmpty() &&
            (Vibhakti.PANCHAMI in context.morphologicalCandidates || Vibhakti.TRTIYA in context.morphologicalCandidates)

    override fun apply(context: VibhaktiRuleContext): VibhaktiRuleResult {
        val v = if (Vibhakti.PANCHAMI in context.morphologicalCandidates) Vibhakti.PANCHAMI else Vibhakti.TRTIYA
        return VibhaktiRuleResult.Assigned(
            v,
            KarakaEvidence(number, text, "$v realizes gambling instrument (2.3.33)."),
        )
    }
}
