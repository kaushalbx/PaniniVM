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
 * Sūtra 6.3.36: क्वणो ङिच्च.
 * Pūrvapada rule for kvan.
 */
object KvanoDicChaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.36",
    text = "क्वणो ङिच्च",
    hindiExplanation = "क्वण् धातु उत्तरपद परे होने पर पूर्वपद को ङित् विधान होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630036,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return last == "क्वण" || last == "क्वण्"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.36 applies ṅit pūrvapada rule before kvan in '$compoundStem'.",
        )
    }
}
