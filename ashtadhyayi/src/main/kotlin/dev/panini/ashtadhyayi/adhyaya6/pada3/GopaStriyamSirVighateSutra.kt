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
 * Sūtra 6.3.39: गोपस्त्रियां शिर्विघाते.
 * Pūrvapada rule for cowherd/shepherd compound terms.
 */
object GopaStriyamSirVighateSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.39",
    text = "गोपस्त्रियां शिर्विघाते",
    hindiExplanation = "गोप स्त्रीलिङ्ग विषय में पूर्वपद ह्रस्व नियम लागू होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630039,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        return first == "गोप" || first == "गोपी"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.39 applies gopa-strī pūrvapada rule in '$compoundStem'.",
        )
    }
}
