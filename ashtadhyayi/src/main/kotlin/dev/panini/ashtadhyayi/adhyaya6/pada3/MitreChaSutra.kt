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
 * Sūtra 6.3.66: मित्रे च.
 * Lengthening / transformation before mitra.
 * Example: विश्वामित्रः (viśvāmitraḥ).
 */
object MitreChaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.66",
    text = "मित्रे च",
    hindiExplanation = "मित्र उत्तरपद परे होने पर विश्व आदि पूर्वपद का दीर्घ आदेश (आ) होता है (उदा. विश्वामित्रः)।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630066,
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
        return (first == "विश्व" || first == "विश्वा") && last == "मित्र"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = "विश्वामित्र"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.66 derives viśvāmitra with pūrvapada lengthening before mitra.",
        )
    }
}
