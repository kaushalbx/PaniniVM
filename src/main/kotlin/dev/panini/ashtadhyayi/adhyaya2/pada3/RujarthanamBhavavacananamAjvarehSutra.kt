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
 * Sūtra 2.3.54 रुजार्थानां भाववचनानामज्वरेः.
 * Assigns Ṣaṣṭhī for the object of verbs denoting disease/pain (except jvara).
 */
object RujarthanamBhavavacananamAjvarehSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.54", text = "रुजार्थानां भाववचनानामज्वरेः",
    hindiExplanation = "ज्वर् धातु को छोड़कर रोग अथवा पीड़ा अर्थ वाली धातुओं के कर्म में षष्ठी विभक्ति होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230054,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            SemanticRelation.DISEASE_PAIN_OBJECT in context.participant?.semanticRelations.orEmpty() &&
            Vibhakti.SASTHI in context.morphologicalCandidates

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.SASTHI,
        KarakaEvidence(number, text, "षष्ठी realizes object of disease/pain verbs (2.3.54)."),
    )
}
