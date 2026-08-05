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
 * Sūtra 6.3.101: द्विगुधनुर्दन्त (Ext registered as 6.3.101).
 * Extended rule for dvigu, dhanus, danta.
 */
object DviguDhanurdantaExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.101",
    text = "द्विगुधनुर्दन्त",
    hindiExplanation = "द्विगु, धनुस् तथा दन्त उत्तरपद परे होने पर महत् पूर्वपद का महा (आत्) आदेश नियम सिद्ध होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630101,
    role = SutraRole.Niyama,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        val last = context.padas.last().upadesha
        return (first == "महत्" || first == "महा") && (last == "धनुस्" || last == "दन्त")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = "महा" + context.padas.drop(1).joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.101 applies mahā substitution before dhanus/danta in '$compoundStem'.",
        )
    }
}
