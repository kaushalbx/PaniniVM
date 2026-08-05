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
 * Sūtra 6.3.69: वा विचारिकायाम्.
 * Optional pūrvapada lengthening in inquiry context.
 */
object VaVicarikayamSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.69",
    text = "वा विचारिकायाम्",
    hindiExplanation = "विचारिका उत्तरपद परे होने पर पूर्वपद का दीर्घ विकल्प से होता है।",
    type = SutraType.VIBHASHA,
    chapter = 6,
    pada = 3,
    optional = true,
    kramaValue = 630069,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return last == "विचारिका"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.69 optionally applies pūrvapada lengthening before vicārikā in '$compoundStem'.",
        )
    }
}
