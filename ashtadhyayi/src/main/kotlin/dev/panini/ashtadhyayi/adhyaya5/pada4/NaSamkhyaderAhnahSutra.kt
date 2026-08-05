package dev.panini.ashtadhyayi.adhyaya5.pada4

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
 * Sūtra 5.4.89: न संख्यादेरह्नः.
 * Prohibition of ahna replacement after numeral-headed pūrvapada.
 * Example: द्व्यहः (dvyahaḥ).
 */
object NaSamkhyaderAhnahSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.89",
    text = "न संख्यादेरह्नः",
    hindiExplanation = "संख्यावाचक पूर्वपद के पश्चात् अहन् का 'अह्न' आदेश नहीं होता (उदा. द्व्यहः, त्र्यहः)।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540089,
    role = SutraRole.Niyama,
    action = SutraAction.NISHEDHA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.DVIGU,
    samasaPriority = 15,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        val last = context.padas.last().upadesha
        return last == "अहन्" && (first == "द्वि" || first == "त्रि" || first == "चतुर्")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        return SamasaRuleResult.NotApplicable
    }
}
