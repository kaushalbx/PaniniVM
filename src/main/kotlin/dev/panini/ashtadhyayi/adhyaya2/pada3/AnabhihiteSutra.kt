package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.core.Vibhakti
import dev.panini.sutra.AdhikaraMetadata
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.vyakaranam.analysis.KarakaEvidence
import dev.panini.vyakaranam.analysis.VibhaktiRuleContext
import dev.panini.vyakaranam.analysis.VibhaktiRuleResult

/** Governing sūtra 2.3.1 अनभिहिते. Rules 2.3.2 to 2.3.73 apply only when the kāraka is unexpressed (anabhihita). */
object AnabhihiteSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.1", text = "अनभिहिते",
    hindiExplanation = "अधिकारोऽयम्। इत ऊर्ध्वं यद्वक्ष्यते तदनभिहिते कर्मत्वादौ वेदितव्यम्।",
    type = SutraType.ADHIKARA, chapter = 2, pada = 3, optional = false, kramaValue = 230001,
    role = SutraRole.Adhikara, action = SutraAction.ADHIKARA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.KARAKA_CANDIDATE),
    adhikara = emptySet(),
    adhikaraMetadata = AdhikaraMetadata(
        endKrama = 230073,
        customStartKrama = 230002,
        isContextEligible = { context ->
            (context as? VibhaktiRuleContext)?.let { !it.abhihita } ?: true
        }
    ),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean = context.abhihita

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.PRATHAMA,
        KarakaEvidence(number, text, "The kāraka is already expressed (abhihita) by verbal inflection; hence non-nominative cases are blocked."),
    )
}
