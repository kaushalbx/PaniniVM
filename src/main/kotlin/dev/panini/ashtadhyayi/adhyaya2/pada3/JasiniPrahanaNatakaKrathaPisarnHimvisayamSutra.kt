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
 * Sūtra 2.3.56 जासिनिप्रहणनाटकक्राथपिषां हिंसायाम्.
 * Assigns Ṣaṣṭhī for the object of jas, niprah, anāṭa, krath, and piṣ when denoting violence/injury.
 */
object JasiniPrahanaNatakaKrathaPisarnHimvisayamSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.56", text = "जासिनिप्रहणनाटकक्राथपिषां हिंसायाम्",
    hindiExplanation = "हिंसा अर्थ में जासि, निप्रहण, नाटक, क्राथ तथा पिष् धातुओं के कर्म में षष्ठी विभक्ति होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230056,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            SemanticRelation.INJURY_VIOLENCE_OBJECT in context.participant?.semanticRelations.orEmpty() &&
            Vibhakti.SASTHI in context.morphologicalCandidates

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.SASTHI,
        KarakaEvidence(number, text, "षष्ठी realizes object of injury/violence verbs (2.3.56)."),
    )
}
