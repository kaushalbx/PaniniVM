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
 * Sūtra 2.3.72 तुल्यार्थैरतुलोपमाभ्यां तृतीयाऽन्यतरस्याम्.
 * Assigns Tṛtīyā (or Ṣaṣṭhī) in connection with words meaning equal (tulya, sadṛśa), excluding tulā and upamā.
 */
object TulyarthairAtulopamabhyamTrtiyanatarasyamSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.72", text = "तुल्यार्थैरतुलोपमाभ्यां तृतीयाऽन्यतरस्याम्",
    hindiExplanation = "तुला और उपमा को छोड़कर तुल्यार्थक शब्दों के योग में तृतीया तथा षष्ठी विभक्ति होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = true, kramaValue = 230072,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            SemanticRelation.EQUAL_COMPARISON in context.participant?.semanticRelations.orEmpty() &&
            (Vibhakti.TRTIYA in context.morphologicalCandidates || Vibhakti.SASTHI in context.morphologicalCandidates)

    override fun apply(context: VibhaktiRuleContext): VibhaktiRuleResult {
        val v = if (Vibhakti.TRTIYA in context.morphologicalCandidates) Vibhakti.TRTIYA else Vibhakti.SASTHI
        return VibhaktiRuleResult.Assigned(
            v,
            KarakaEvidence(number, text, "$v realizes equal comparison (2.3.72)."),
        )
    }
}
