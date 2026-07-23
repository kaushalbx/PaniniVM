package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.core.Karaka
import dev.panini.core.Vibhakti
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.vyakaranam.analysis.KarakaEvidence
import dev.panini.vyakaranam.analysis.VibhaktiRuleContext
import dev.panini.vyakaranam.analysis.VibhaktiRuleResult

/**
 * Sūtra 2.3.12 गत्यर्थकर्मणि द्वितीयाचतुर्थ्यौ चेष्टायामनध्वनि.
 * Assigns either Dvitīyā or Caturthī to the destination of verbs of motion (gatyartha).
 */
object GatyarthaKarmaniDvitiyaCaturthyauSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.12", text = "गत्यर्थकर्मणि द्वितीयाचतुर्थ्यौ चेष्टायामनध्वनि",
    hindiExplanation = "चेष्टायुक्तध्वभिन्नगत्यर्थकर्मण्यनभिहिते द्वितीयाचतुर्थ्यौ स्तः।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = true, kramaValue = 230012,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.KARAKA, SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            context.karaka == Karaka.KARMAN &&
            (Vibhakti.DVITIYA in context.morphologicalCandidates || Vibhakti.CHATURTHI in context.morphologicalCandidates)

    override fun apply(context: VibhaktiRuleContext): VibhaktiRuleResult {
        val caseAssigned = if (Vibhakti.CHATURTHI in context.morphologicalCandidates) Vibhakti.CHATURTHI else Vibhakti.DVITIYA
        return VibhaktiRuleResult.Assigned(
            caseAssigned,
            KarakaEvidence(number, text, "$caseAssigned assigns motion goal / destination (2.3.12 गत्यर्थकर्मणि)."),
        )
    }
}
