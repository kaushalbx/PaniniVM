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
 * Sūtra 6.3.100: महतः समानाधिकरणे (Ext registered as 6.3.100).
 * Extended rule for mahat in apposition.
 */
object MahatahSamanadhikaranaExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.100",
    text = "महतः समानाधिकरणे",
    hindiExplanation = "समानाधिकरण उत्तरपद परे होने पर महत् पूर्वपद का महा (आत्) आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630100,
    role = SutraRole.Niyama,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.KARMADHARAYA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        return first == "महत्" || first == "महान्"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = "महा" + context.padas.drop(1).joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.100 substitutes mahā for mahat in apposition in '$compoundStem'.",
        )
    }
}
