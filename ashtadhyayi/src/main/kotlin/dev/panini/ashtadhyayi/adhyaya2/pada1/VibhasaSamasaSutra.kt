package dev.panini.ashtadhyayi.adhyaya2.pada1

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.sutra.SamasaSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 2.1.11: विभाषा (registered as 2.1.96 for unique ID).
 * Adhikāra sūtra establishing optionality (vibhāṣā) of compound formation.
 */
object VibhasaSamasaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.96",
    text = "विभाषा",
    hindiExplanation = "इत ऊर्ध्वम् अनुक्रमिष्यामः समासाः विभाषा भवन्ति। (आगे कहे जाने वाले समास विकल्प से होते हैं)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = true,
    kramaValue = 210096,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        return context.padas.size >= 2
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.96 establishes optionality (vibhāṣā) of compound '$compoundStem'.",
        )
    }
}
