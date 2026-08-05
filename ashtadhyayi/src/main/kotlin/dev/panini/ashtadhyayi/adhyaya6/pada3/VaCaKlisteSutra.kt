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
 * Sūtra 6.3.73: वा च क्लिष्टे.
 * Optional rule before kliṣṭa.
 */
object VaCaKlisteSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.73",
    text = "वा च क्लिष्टे",
    hindiExplanation = "क्लिष्ट उत्तरपद परे होने पर पूर्वपद नियम विकल्प से लागू होता है।",
    type = SutraType.VIBHASHA,
    chapter = 6,
    pada = 3,
    optional = true,
    kramaValue = 630073,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return last == "क्लिष्ट"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.73 optionally applies pūrvapada rule before kliṣṭa in '$compoundStem'.",
        )
    }
}
