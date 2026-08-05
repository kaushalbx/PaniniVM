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
 * Sūtra 6.3.30: दिवश्च.
 * Aluk for div before pṛthivī (divāpṛthivyau).
 */
object DivasChaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.30",
    text = "दिवश्च",
    hindiExplanation = "पृथ्वी उत्तरपद परे होने पर दिव् शब्द का 'दिवा' रूप सिद्ध (अलुक्/दीर्घ) होता है (उदा. दिवापृथिव्यौ)।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630030,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.DVANDVA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        val last = context.padas.last().upadesha
        return (first == "दिव्" || first == "दिवा") && (last == "पृथ्वी" || last == "पृथिवी")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val last = context.padas.last().upadesha
        val compoundStem = "दिवा" + last
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.30 preserves divā before $last in '$compoundStem'.",
        )
    }
}
