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
 * Sūtra 6.3.20: अनुगव्यं च.
 * Aluk / stem rule for anugavam.
 */
object AnugavyamChaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.20",
    text = "अनुगव्यं च",
    hindiExplanation = "अनुगव्य शब्द निपातन से अलुक्/दीर्घ सिद्ध होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630020,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.AVYAYIBHAVA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        val last = context.padas.last().upadesha
        return context.samasaType == SamasaType.AVYAYIBHAVA && first == "अनु" && (last == "गो" || last == "गव")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = "अनुगव्य"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.20 derives nipātana form '$compoundStem'.",
        )
    }
}
