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
 * Sūtra 2.3.14 क्रियार्थोपपदस्य च कर्मणि स्थानिनः.
 * Assigns Caturthī to the unexpressed karma of an implied verb of motion/purpose (tumun-arthaka).
 */
object KriyarthopapadasyaCaKarmaniSthaninahSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.14", text = "क्रियार्थोपपदस्य च कर्मणि स्थानिनः",
    hindiExplanation = "क्रियार्थक उपपद (तुमुन्-प्रत्ययान्त क्रिया) के अप्रयुक्त कर्म में चतुर्थी विभक्ति होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230014,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            SemanticRelation.IMPLIED_PURPOSE_OBJECT in context.participant?.semanticRelations.orEmpty() &&
            Vibhakti.CHATURTHI in context.morphologicalCandidates

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.CHATURTHI,
        KarakaEvidence(number, text, "चतुर्थी realizes object of implied purpose verb (2.3.14)."),
    )
}
