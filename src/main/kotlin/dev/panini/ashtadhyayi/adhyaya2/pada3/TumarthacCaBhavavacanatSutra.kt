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
 * Sūtra 2.3.15 तुमर्थाच्च भाववचनात्.
 * Assigns Caturthī to a verbal noun expressing action used in the sense of the tumun suffix.
 */
object TumarthacCaBhavavacanatSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.15", text = "तुमर्थाच्च भाववचनात्",
    hindiExplanation = "तुमुन्-प्रत्यय के अर्थ वाले भाववाचक शब्द से चतुर्थी विभक्ति होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230015,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            SemanticRelation.PURPOSE_ACTION in context.participant?.semanticRelations.orEmpty() &&
            Vibhakti.CHATURTHI in context.morphologicalCandidates

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.CHATURTHI,
        KarakaEvidence(number, text, "चतुर्थी realizes purpose action noun (2.3.15)."),
    )
}
