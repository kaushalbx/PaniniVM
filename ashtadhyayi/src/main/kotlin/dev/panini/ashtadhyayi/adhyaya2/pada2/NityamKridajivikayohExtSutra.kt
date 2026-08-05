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
 * Sūtra 2.2.17: नित्यं क्रीडाजीविकयोः (registered as 2.2.97 for unique ID).
 * Prescribes mandatory Upapada compound in sports/games and livelihoods.
 * Example: उद्दालकपुष्पभञ्जिका.
 */
object NityamKridajivikayohExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.97",
    text = "नित्यं क्रीडाजीविकयोः",
    hindiExplanation = "क्रीडा और जीविका अर्थ में उपपद तत्पुरुष समास नित्य (नित्यम्) होता है (उदा. उद्दालकपुष्पभञ्जिका)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220097,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.UPAPADA_TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        return context.padas.size >= 2 && context.samasaType == SamasaType.UPAPADA_TATPURUSA
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.2.97 forms mandatory Upapada compound for sport/livelihood '$compoundStem'.",
        )
    }
}
