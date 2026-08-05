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
 * Sūtra 6.3.58: स्पृशोऽनुदके क्विन्.
 * Substitutions before root spṛś when not referring to water.
 */
object SprsoAnudakeKvinSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.58",
    text = "स्पृशोऽनुदके क्विन्",
    hindiExplanation = "अनुदक (जल से भिन्न) अर्थ में स्पृश् उत्तरपद परे होने पर क्विन् प्रत्यय और पूर्वपद नियम लागू होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630058,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return last == "स्पृश्" || last == "स्पृक्"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.58 applies spṛś non-water rule in '$compoundStem'.",
        )
    }
}
