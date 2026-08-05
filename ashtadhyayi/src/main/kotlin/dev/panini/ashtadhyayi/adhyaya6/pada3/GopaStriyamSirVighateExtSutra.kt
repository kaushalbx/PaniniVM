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
 * Sūtra 6.3.93: गोपस्त्रियां शिरविघाते (Ext registered as 6.3.93).
 * Extended rule for cowherd/feminine head-striking.
 */
object GopaStriyamSirVighateExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.93",
    text = "गोपस्त्रियां शिरविघाते",
    hindiExplanation = "शिरोविघात (सिर पर चोट) विषय में गोप तथा स्त्री उत्तरपद परे होने पर पूर्वपद नियम लागू होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630093,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return last == "गोप" || last == "स्त्री"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.93 applies head-striking pūrvapada rule in '$compoundStem'.",
        )
    }
}
