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
 * Sūtra 6.3.42: तसिमत्स्वर्थेष्वभावः.
 * Limitation / prohibition of puṁvadbhāva in tasi, matup, etc. contexts.
 */
object TasimatSvarnthesvAbhavahSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.42",
    text = "तसिमत्स्वर्थेष्वभावः",
    hindiExplanation = "तसिल्, मतुप् आदि प्रत्ययों तथा तदर्थ में पुंवद्भाव का अभाव (प्रतिषेध) होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630042,
    role = SutraRole.Niyama,
    action = SutraAction.NISHEDHA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 15,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return last == "तस्" || last == "मत्" || last == "वत्"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        return SamasaRuleResult.NotApplicable
    }
}
