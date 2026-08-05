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
 * Sūtra 6.3.38: क्युन्नद्योर्मानिण्याम्.
 * Shortening / feminine substitute before māninī.
 */
object KyunNadyorManinyamSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.38",
    text = "क्युन्नद्योर्मानिण्याम्",
    hindiExplanation = "मानिनी उत्तरपद परे होने पर क्युन् तथा नदीसंज्ञक पूर्वपद का ह्रस्व होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630038,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return last == "मानिनी" || last == "मानिन्"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.38 applies shortening before māninī in '$compoundStem'.",
        )
    }
}
