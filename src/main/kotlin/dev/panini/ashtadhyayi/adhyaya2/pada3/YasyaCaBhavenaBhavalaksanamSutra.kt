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

/** Sūtra 2.3.37 यस्य च भावेन भावलक्षणम्. Assigns Saptamī (Sati-Saptamī / absolute locative) for action-marking. */
object YasyaCaBhavenaBhavalaksanamSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.37", text = "यस्य च भावेन भावलक्षणम्",
    hindiExplanation = "यस्य क्रियया क्रियान्तरं लक्ष्यते ततः सप्तमी स्यात् (सति-सप्तमी)।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230037,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.KARAKA, SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            context.karaka == Karaka.ANIRDHARITA &&
            Vibhakti.SAPTAMI in context.morphologicalCandidates

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.SAPTAMI,
        KarakaEvidence(number, text, "सप्तमी realizes absolute locative action marking (2.3.37 सति-सप्तमी)."),
    )
}
