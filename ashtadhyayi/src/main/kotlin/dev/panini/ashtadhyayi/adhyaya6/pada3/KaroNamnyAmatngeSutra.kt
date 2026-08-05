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
 * Sūtra 6.3.15: कारो नाम्न्यमातङ्गे.
 * Prescribes Aluk of case affix before kāra in proper names (not meaning elephant).
 */
object KaroNamnyAmatngeSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.15",
    text = "कारो नाम्न्यमातङ्गे",
    hindiExplanation = "नाम (संज्ञा) विषय में कार उत्तरपद परे होने पर पूर्वपद विभक्ति का अलुक् होता है (मातङ्ग अर्थ को छोड़कर)।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630015,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.ALUK_TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return (context.samasaType == SamasaType.ALUK_TATPURUSA || context.samasaType == SamasaType.UPAPADA_TATPURUSA) && last == "कार"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.15 preserves case affix (Aluk) before kāra in '$compoundStem'.",
        )
    }
}
