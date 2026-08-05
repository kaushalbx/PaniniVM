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
 * Sūtra 5.4.97: संख्यापूर्वी रात्रं क्लीबम्.
 * Neuter gender rule for numeral-preceded rātra compound.
 * Example: द्विरात् (dvirātram).
 */
object SamkhyapurvamRatramKlibamSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.97",
    text = "संख्यापूर्वी रात्रं क्लीबम्",
    hindiExplanation = "संख्या पूर्व में होने पर रात्र समास नपुंसकलिङ्ग (क्लीव) होता है (उदा. द्विरात्)।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540097,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.DVIGU,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return context.samasaType == SamasaType.DVIGU && (last == "रात्र" || last == "रात्रि")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.first().upadesha + "रात्र"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.97 enforces neuter gender for numeral-preceded rātra in '$compoundStem'.",
        )
    }
}
