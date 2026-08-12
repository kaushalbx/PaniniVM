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
 * Sūtra 6.3.87: पित्रामाता (Ext registered as 6.3.87).
 * Extended compound form pitrāmātā.
 */
object PitramataExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.87",
    text = "पित्रामाता",
    hindiExplanation = "पितृ तथा मातृ द्वन्द्व समास में 'पित्रामाता' निपातन से सिद्ध होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630087,
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
        return first == "पितृ" && last == "मातृ"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = "पित्रामातृ"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.87 derives nipātana form pitrāmātā.",
        )
    }
}
