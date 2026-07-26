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
 * Sūtra 2.3.34 स्तोकानल्पकृच्छ्रकतिपयस्यासत्त्ववचनस्य.
 * Assigns Pañcamī or Tṛtīyā for non-substance words stoka, alpa, kṛcchra, katipaya.
 */
object StokanAlpaKrcchraSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.34", text = "स्तोकानल्पकृच्छ्रकतिपयस्यासत्त्ववचनस्य",
    hindiExplanation = "द्रव्य-भिन्न अर्थ वाले स्तोक, अल्प, कृच्छ्र तथा कतिपय शब्दों में पञ्चमी अथवा तृतीया विभक्ति होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = true, kramaValue = 230034,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            SemanticRelation.INDETERMINATE_QUANTITY in context.participant?.semanticRelations.orEmpty() &&
            (Vibhakti.PANCHAMI in context.morphologicalCandidates || Vibhakti.TRTIYA in context.morphologicalCandidates)

    override fun apply(context: VibhaktiRuleContext): VibhaktiRuleResult {
        val v = if (Vibhakti.PANCHAMI in context.morphologicalCandidates) Vibhakti.PANCHAMI else Vibhakti.TRTIYA
        return VibhaktiRuleResult.Assigned(
            v,
            KarakaEvidence(number, text, "$v realizes non-substance quantity (2.3.34)."),
        )
    }
}
