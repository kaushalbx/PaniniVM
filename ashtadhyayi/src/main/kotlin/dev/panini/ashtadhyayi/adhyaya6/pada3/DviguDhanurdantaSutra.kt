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
 * Sūtra 6.3.47 / Ext: द्विगुधनुर्दन्तकमलमुखेषु (registered as 6.3.147 for unique ID).
 * Pūrvapada substitution / lengthening before dhanus, danta, kamala, mukha.
 */
object DviguDhanurdantaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.147",
    text = "द्विगुधनुर्दन्तकमलमुखेषु",
    hindiExplanation = "द्विगु, धनुष्, दन्त, कमल, मुख उत्तरपद परे होने पर पूर्वपद नियम लागू होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630147,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.BAHUVRIHI,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return context.samasaType == SamasaType.DVIGU || last == "धनुष्" || last == "दन्त" || last == "कमल" || last == "मुख"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.147 applies pūrvapada rule before dhanus/danta/kamala/mukha in '$compoundStem'.",
        )
    }
}
