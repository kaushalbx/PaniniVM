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
 * Sūtra 6.3.55: पादस्य पद्.
 * General substitution of pad for pāda.
 * Example: द्विपाद् (dvipād), त्रिपाद् (tripād).
 */
object PadasyaPadSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.55",
    text = "पादस्य पद्",
    hindiExplanation = "पाद पूर्वपद/उत्तरपद के स्थान पर 'पद्' आदेश होता है (उदा. द्विपाद्)।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630055,
    role = SutraRole.Niyama,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.BAHUVRIHI,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return last == "पाद" || last == "पाद्"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val first = context.padas.first().upadesha
        val compoundStem = first + "पद्"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.55 substitutes pad for pāda in '$compoundStem'.",
        )
    }
}
