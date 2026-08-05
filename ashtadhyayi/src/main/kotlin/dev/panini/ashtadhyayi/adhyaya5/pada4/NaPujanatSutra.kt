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
 * Sūtra 5.4.79: न पूजनात्.
 * Prohibition of Samāsānta suffix after praised/honored words (e.g., su-, ati-).
 * Example: सुराजा (surājā).
 */
object NaPujanatSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.79",
    text = "न पूजनात्",
    hindiExplanation = "पूजा (प्रशंसा) वाची अति, सु आदि से उत्तर समासान्त प्रत्यय नहीं होता है (उदा. सुराजा)।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540079,
    role = SutraRole.Niyama,
    action = SutraAction.NISHEDHA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 15,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        return first == "सु" || first == "अति"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        return SamasaRuleResult.NotApplicable
    }
}
