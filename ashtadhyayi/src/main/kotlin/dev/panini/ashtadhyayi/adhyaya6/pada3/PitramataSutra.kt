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
 * Sūtra 6.3.33: पित्रा माता.
 * Pūrvapada rule for pitṛ before mātṛ in Devatā-dvandva.
 * Example: पितामातरौ (pitāmātarau).
 */
object PitramataSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.33",
    text = "पित्रा माता",
    hindiExplanation = "माता उत्तरपद परे होने पर पितृ शब्द का पिता रूप सिद्ध होता है (उदा. पितामातरौ)।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630033,
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
        return (first == "पितृ" || first == "पिता") && (last == "मातृ" || last == "माता")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = "पितामातृ"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.33 forms pitāmātṛ in '$compoundStem'.",
        )
    }
}
