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
 * Sūtra 6.3.57: निष्ठायां च.
 * Substitution before niṣṭhā (kta/ktavatu) participles.
 */
object NisthayamChaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.57",
    text = "निष्ठायां च",
    hindiExplanation = "निष्ठा (क्त, क्तवतु) प्रत्ययान्त उत्तरपद परे होने पर पूर्वपद आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630057,
    role = SutraRole.Niyama,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return last.endsWith("त") || last.endsWith("तवत्")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.57 applies substitution before niṣṭhā participle in '$compoundStem'.",
        )
    }
}
