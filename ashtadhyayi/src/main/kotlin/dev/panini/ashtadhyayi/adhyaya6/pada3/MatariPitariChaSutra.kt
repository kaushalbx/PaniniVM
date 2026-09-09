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
 * Sūtra 6.3.32: मातरपितरावुदीचाम्.
 * Northern usage optionally preserves the r-form in the first member.
 */
object MatariPitariChaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.32",
    text = "मातरपितरावुदीचाम्",
    hindiExplanation = "उदीचों के प्रयोग में मातृ-पितृ द्वन्द्व का मातरपितरौ रूप होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630032,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.DVANDVA,
    samasaPriority = 30,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        val last = context.padas.last().upadesha
        return context.samasaType == SamasaType.DVANDVA &&
            (first == "मातृ" || first == "माता") && (last == "पितृ" || last == "पिता")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = "मातरपितृ"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.32 forms the northern variant mātarapitṛ in '$compoundStem'.",
            memberEdits = mapOf(0 to "मातर",1 to "पितृ"),
        )
    }
}
