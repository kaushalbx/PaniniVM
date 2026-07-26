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
 * Sūtra 2.3.10 पञ्चम्यपाङ्परिभिः.
 * Assigns Pañcamī in connection with karmapravacanīyas ap, āṅ, and pari.
 */
object PancamyApangParibhihSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.10", text = "पञ्चम्यपाङ्परिभिः",
    hindiExplanation = "अप, आङ्, परि कर्मप्रवचनीय के योग में पञ्चमी होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230010,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            SemanticRelation.EXCLUSION_LIMIT in context.participant?.semanticRelations.orEmpty() &&
            Vibhakti.PANCHAMI in context.morphologicalCandidates

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.PANCHAMI,
        KarakaEvidence(number, text, "पञ्चमी realizes relation with ap/āṅ/pari karmapravacanīya (2.3.10)."),
    )
}
