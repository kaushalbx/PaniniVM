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
import dev.panini.analysis.PurposeActionGovernor
import dev.panini.core.Karaka

/**
 * Sūtra 2.3.14 क्रियार्थोपपदस्य च कर्मणि स्थानिनः.
 * Assigns Caturthī to the object belonging to an omitted purpose verb.
 */
object KriyarthopapadasyaCaKarmaniSthaninahSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.14", text = "क्रियार्थोपपदस्य च कर्मणि स्थानिनः",
    hindiExplanation = "क्रियार्थक उपपद से सम्बद्ध अप्रयुक्त धातु के कर्म में चतुर्थी विभक्ति होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230014,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.SEMANTIC_FEATURE, SutraInput.KARAKA, SutraInput.DHATU),
    adhikara = setOf("2.3.1"),
    anuvrtti = setOf("2.3.13"),
) {
    fun hasImpliedPurposeObject(context: VibhaktiRuleContext): Boolean =
        context.karaka == Karaka.KARMAN && context.participant?.purposeGovernor is PurposeActionGovernor.Implied

    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            hasImpliedPurposeObject(context) &&
            Vibhakti.CHATURTHI in context.morphologicalCandidates

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.CHATURTHI,
        KarakaEvidence(number, text, "चतुर्थी realizes the karman of omitted purpose action " +
            "${context.participant?.purposeGovernor?.id}: ${context.participant?.purposeGovernor?.dhatu} (2.3.14)."),
    )
}
