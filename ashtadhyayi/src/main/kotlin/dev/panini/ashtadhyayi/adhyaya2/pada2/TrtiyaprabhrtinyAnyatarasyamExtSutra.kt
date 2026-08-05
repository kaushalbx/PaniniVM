package dev.panini.ashtadhyayi.adhyaya2.pada2

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
 * Sūtra 2.2.21: तृतीयाप्रभृतीन्यन्यतरस्याम्.
 * Rules optional retention of case affixes for 3rd case onwards in Avyayībhāva.
 */
object TrtiyaprabhrtinyAnyatarasyamExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.21",
    text = "तृतीयाप्रभृतीन्यन्यतरस्याम्",
    hindiExplanation = "अव्ययीभाव समास में तृतीया विभक्ति से लेकर आगे की विभक्तियों में विकल्प से अमुक्-भाव होता है।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = true,
    kramaValue = 220021,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.AVYAYIBHAVA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        return context.padas.size >= 2 &&
            context.samasaType == SamasaType.AVYAYIBHAVA
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.2.21 allows optional case retention for 3rd case onwards in Avyayībhāva '$compoundStem'.",
        )
    }
}
