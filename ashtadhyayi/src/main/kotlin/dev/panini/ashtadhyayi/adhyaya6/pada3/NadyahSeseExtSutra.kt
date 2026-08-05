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
 * Sūtra 6.3.102: नद्याः शेषे (Ext registered as 6.3.102).
 * Extended rule for remaining nadī stems.
 */
object NadyahSeseExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.102",
    text = "नद्याः शेषे",
    hindiExplanation = "शेष उत्तरपद परे होने पर नदीसंज्ञक पूर्वपद का ह्रस्व आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630102,
    role = SutraRole.Niyama,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        return first.endsWith("ी") || first.endsWith("ू")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.102 applies nadī stem shortening in '$compoundStem'.",
        )
    }
}
