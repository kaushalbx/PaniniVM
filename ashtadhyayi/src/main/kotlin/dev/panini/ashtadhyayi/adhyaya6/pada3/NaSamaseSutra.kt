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
 * Sūtra 6.3.37: न समासे.
 * Prohibition of pūrvapada lengthening in certain compound formations.
 */
object NaSamaseSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.37",
    text = "न समासे",
    hindiExplanation = "समास में पूर्वपद के दीर्घ आदेश का प्रतिषेध (निषेध) होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630037,
    role = SutraRole.Niyama,
    action = SutraAction.NISHEDHA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 15,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        return context.samasaType == SamasaType.TATPURUSA
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        return SamasaRuleResult.NotApplicable
    }
}
