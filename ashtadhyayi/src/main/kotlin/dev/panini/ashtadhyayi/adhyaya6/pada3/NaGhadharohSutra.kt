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
 * Sūtra 6.3.45: न घाधरोः.
 * Prohibition of puṁvadbhāva before gha (tarap, tamap) or dhara.
 */
object NaGhadharohSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.45",
    text = "न घाधरोः",
    hindiExplanation = "घ (तरप्, तमप्) तथा धर उत्तरपद परे होने पर पुंवद्भाव नहीं होता।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630045,
    role = SutraRole.Niyama,
    action = SutraAction.NISHEDHA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 15,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return last == "तर" || last == "तरा" || last == "तम" || last == "तमा" || last == "धर"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        return SamasaRuleResult.NotApplicable
    }
}
