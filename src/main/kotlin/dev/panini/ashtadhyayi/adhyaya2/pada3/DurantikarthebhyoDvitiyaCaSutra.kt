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
 * Sūtra 2.3.35 दूरान्तिकार्थेभ्यो द्वितीया च.
 * Assigns Pañcamī, Tṛtīyā, Saptamī, or Dvitīyā for words expressing distance or proximity.
 */
object DurantikarthebhyoDvitiyaCaSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.35", text = "दूरान्तिकार्थेभ्यो द्वितीया च",
    hindiExplanation = "दूर तथा अन्तिक अर्थ वाले शब्दों में पञ्चमी, तृतीया, सप्तमी तथा द्वितीया विभक्ति होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = true, kramaValue = 230035,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            SemanticRelation.DISTANCE_OR_PROXIMITY in context.participant?.semanticRelations.orEmpty() &&
            (Vibhakti.PANCHAMI in context.morphologicalCandidates ||
                Vibhakti.TRTIYA in context.morphologicalCandidates ||
                Vibhakti.SAPTAMI in context.morphologicalCandidates ||
                Vibhakti.DVITIYA in context.morphologicalCandidates)

    override fun apply(context: VibhaktiRuleContext): VibhaktiRuleResult {
        val v = listOf(Vibhakti.PANCHAMI, Vibhakti.TRTIYA, Vibhakti.SAPTAMI, Vibhakti.DVITIYA)
            .firstOrNull { it in context.morphologicalCandidates } ?: Vibhakti.PANCHAMI
        return VibhaktiRuleResult.Assigned(
            v,
            KarakaEvidence(number, text, "$v realizes distance or proximity relation (2.3.35)."),
        )
    }
}
