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
 * Sūtra 6.3.86: मातरि पितरि च (Ext registered as 6.3.86).
 * Extended rule for parents (mātari-pitari).
 */
object MatariPitariExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.86",
    text = "मातरि पितरि च",
    hindiExplanation = "मातरि तथा पितरि द्वन्द्व समास में आनङ् आदेश नियम सिद्ध होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630086,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.DVANDVA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        val last = context.padas.last().upadesha
        return (first == "मातृ" || first == "मातरि") && (last == "पितृ" || last == "पितरि")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = "मातरापितृ"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.86 derives mātarāpitarā parent dvandva form.",
        )
    }
}
