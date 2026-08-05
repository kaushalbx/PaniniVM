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
 * Sūtra 6.3.35: ताच्छील्ये वः.
 * Pūrvapada lengthening in habitual/dispositional compounds.
 */
object TacchilyeVahSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.35",
    text = "ताच्छील्ये वः",
    hindiExplanation = "ताच्छील्य (स्वाभाव्य) अर्थ में पूर्वपद का दीर्घ/विकल्प विधान होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630035,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        return context.samasaType == SamasaType.TATPURUSA
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.35 applies habitual pūrvapada rule for '$compoundStem'.",
        )
    }
}
