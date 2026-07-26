package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.core.Karaka
import dev.panini.core.Vibhakti
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.analysis.KarakaEvidence
import dev.panini.analysis.VibhaktiRuleContext
import dev.panini.analysis.VibhaktiRuleResult

/** Sūtra 2.3.50 षष्ठी शेषे. Assigns Ṣaṣṭhī for remaining non-kāraka relations (possession, relationship, etc.). */
object SasthiSeseSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.50", text = "षष्ठी शेषे",
    hindiExplanation = "कमीद्यतिरिक्तः स्वस्वामिसम्बन्धादिः शेषः, तत्र षष्ठी स्यात्।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230050,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            (context.karaka == Karaka.ANIRDHARITA) &&
            Vibhakti.SASTHI in context.morphologicalCandidates

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.SASTHI,
        KarakaEvidence(number, text, "षष्ठी realizes non-kāraka relational connection (2.3.50 षष्ठी शेषे)."),
    )
}
