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
 * 2.3.52: adhīgartha-dayeśāṁ karmaṇi.
 * Assigns genitive (Ṣaṣṭhī) to the object of remembering (adhī), pitying (day), or ruling (īś).
 */
object AdhigarthaDayesamKarmaniSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.52",
    text = "अधीगर्थदयेशां कर्मणि",
    hindiExplanation = "स्मरणार्थक (अधी), दयाार्थक (दयी) और ईशनार्थक (ईश्) धातुओं के कर्म में षष्ठी विभक्ति होती है।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 3,
    optional = false,
    kramaValue = 230052,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.KARAKA)
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        Vibhakti.SASTHI in context.morphologicalCandidates &&
            context.participant?.semanticRelations?.contains(SemanticRelation.MEMORY_OR_RULING_OBJECT) == true

    override fun apply(context: VibhaktiRuleContext): VibhaktiRuleResult =
        VibhaktiRuleResult.Assigned(
            Vibhakti.SASTHI,
            KarakaEvidence(number, text, "Assigns Ṣaṣṭhī for object of remembering/pitying/ruling verbs.")
        )
}
