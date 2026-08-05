package dev.panini.ashtadhyayi.adhyaya6.pada3

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
 * Sūtra 6.3.50: ह्रस्वो नपुंसके प्रातिपदिकस्य.
 * Prescribes shortening of nominal stem in neuter compounds.
 * Example: उपगु (upagu), निष्कौशाम्बि (niṣkauśāmbi).
 */
object HrasvoNapumsakePratipadikasyaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.50",
    text = "ह्रस्वो नपुंसके प्रातिपदिकस्य",
    hindiExplanation = "नपुंसकलिङ्ग प्रातिपदिक के अन्त को ह्रस्व आदेश होता है (उदा. उपगु)।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630050,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.AVYAYIBHAVA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        return context.samasaType == SamasaType.AVYAYIBHAVA
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.50 applies neuter stem shortening in '$compoundStem'.",
        )
    }
}
