package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.analysis.VibhaktiRuleContext
import dev.panini.analysis.VibhaktiRuleResult

/** Governs anabhihita case prescriptions; it does not itself assign prathamā. */
object AnabhihiteSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.1", text = "अनभिहिते",
    hindiExplanation = "अधिकारोऽयम्। इत ऊर्ध्वं यद्वक्ष्यते तदनभिहिते कर्मत्वादौ वेदितव्यम्।",
    type = SutraType.ADHIKARA, chapter = 2, pada = 3, optional = false, kramaValue = 230001,
    role = SutraRole.Adhikara(
        endKrama = 230073,
        customStartKrama = 230002,
        isContextEligible = { context ->
            (context as? VibhaktiRuleContext)?.let { !it.abhihita } ?: true
        }
    ),
    action = SutraAction.ADHIKARA, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.KARAKA_CANDIDATE),
    adhikara = emptySet(),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean = false

    override fun apply(context: VibhaktiRuleContext): VibhaktiRuleResult =
        throw UnsupportedOperationException("2.3.1 is an adhikāra; it does not assign a participant's vibhakti.")
}
