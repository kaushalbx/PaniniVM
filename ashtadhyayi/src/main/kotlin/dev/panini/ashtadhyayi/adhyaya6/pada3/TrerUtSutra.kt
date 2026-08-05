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
 * Sūtra 6.3.24: त्रेरुट्.
 * Prescribes uṭ substitution for tri before certain suffixes/उत्तरपद.
 */
object TrerUtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.24",
    text = "त्रेरुट्",
    hindiExplanation = "उत्तरपद परे होने पर त्रि शब्द को उट् आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630024,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.DVIGU,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        return first == "त्रि"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val last = context.padas.last().upadesha
        val compoundStem = "त्रयु" + last
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.24 applies uṭ substitution for tri in '$compoundStem'.",
        )
    }
}
