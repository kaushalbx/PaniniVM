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
 * Sūtra 6.3.52: पादस्याङ्गालोपोऽहस्तिनि.
 * Substitution of pad for pāda in non-elephant contexts.
 */
object PadasyangalopoAhastiniSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.52",
    text = "पादस्याङ्गालोपोऽहस्तिनि",
    hindiExplanation = "हस्ती से भिन्न अर्थ में पाद पूर्वपद को 'पद्' आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630052,
    role = SutraRole.Niyama,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.BAHUVRIHI,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        return first == "पाद"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val last = context.padas.last().upadesha
        val compoundStem = "पद्" + last
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.52 substitutes pad for pāda in non-elephant context in '$compoundStem'.",
        )
    }
}
