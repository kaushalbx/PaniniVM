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
 * Sūtra 5.4.112: राज्ञः खार्याम्.
 * Samāsānta after rājan before khārī.
 */
object RajnahKharyamSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.112",
    text = "राज्ञः खार्याम्",
    hindiExplanation = "खारी उत्तरपद परे होने पर राजन् शब्द से समासान्त प्रत्यय होता है।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540112,
    role = SutraRole.Niyama,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        val last = context.padas.last().upadesha
        return (first == "राजन्" || first == "राज") && (last == "खारी" || last == "खार")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = "राजखार"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.112 adds Samāsānta after rājan before khārī in '$compoundStem'.",
        )
    }
}
