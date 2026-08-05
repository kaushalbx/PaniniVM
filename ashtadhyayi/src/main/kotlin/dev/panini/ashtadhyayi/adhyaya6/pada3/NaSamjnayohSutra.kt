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
 * Sūtra 6.3.49 / Ext: न संज्ञायाम् (registered as 6.3.149 for unique ID).
 * Prohibition of shortening in proper names / titles.
 */
object NaSamjnayohSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.149",
    text = "न संज्ञायाम्",
    hindiExplanation = "संज्ञा (नाम) विषय में पूर्वपद के ह्रस्व का प्रतिषेध (निषेध) होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630149,
    role = SutraRole.Niyama,
    action = SutraAction.NISHEDHA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 15,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        return first.endsWith("ी") || first.endsWith("ू")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        return SamasaRuleResult.NotApplicable
    }
}
