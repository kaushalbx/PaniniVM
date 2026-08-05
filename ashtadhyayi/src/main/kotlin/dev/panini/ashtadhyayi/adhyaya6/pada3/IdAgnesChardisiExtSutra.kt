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
 * Sūtra 6.3.83: इदग्नेश्छर्दिसि (Ext registered as 6.3.83).
 * Extended rule for agni before chardis.
 */
object IdAgnesChardisiExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.83",
    text = "इदग्नेश्छर्दिसि",
    hindiExplanation = "छर्दिस उत्तरपद परे होने पर अग्नि पूर्वपद का ईत् (ई) आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630083,
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
        return first == "अग्नि" && last == "छर्दिस"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = "अग्नीछर्दिस"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.83 substitutes ī for agni before chardis.",
        )
    }
}
