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
 * Sūtra 5.4.111: रात्रे्रन्तः.
 * Optional Samāsānta for rātri at end of compound.
 */
object RatrerAntasSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.111",
    text = "रात्रे्रन्तः",
    hindiExplanation = "समास के अन्त में रात्रि शब्द से समासान्त प्रत्यय विकल्प से होता है।",
    type = SutraType.VIBHASHA,
    chapter = 5,
    pada = 4,
    optional = true,
    kramaValue = 540111,
    role = SutraRole.Niyama,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return last == "रात्रि"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.first().upadesha + "रात्र"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.111 optionally replaces rātri with rātra in '$compoundStem'.",
        )
    }
}
