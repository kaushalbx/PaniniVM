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
 * Sūtra 6.3.59: पादोऽन्यतरस्याम्.
 * Optional substitution of pad for pāda.
 */
object PadoAnyatarasyamSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.59",
    text = "पादोऽन्यतरस्याम्",
    hindiExplanation = "पाद पूर्वपद/उत्तरपद के स्थान पर 'पद्' आदेश विकल्प से होता है।",
    type = SutraType.ANYATARASYAM,
    chapter = 6,
    pada = 3,
    optional = true,
    kramaValue = 630059,
    role = SutraRole.Niyama,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.BAHUVRIHI,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return last == "पाद"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val first = context.padas.first().upadesha
        val compoundStem = first + "पद्"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.59 optionally substitutes pad for pāda in '$compoundStem'.",
        )
    }
}
