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
 * Sūtra 5.4.70: अञ्चेरुपसर्गस्य.
 * Prescribes Samāsānta a-pratyaya for añc root preceded by an Upasarga.
 * Example: प्राक्, प्राच्यम्.
 */
object AncerUpasargasyaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.70",
    text = "अञ्चेरुपसर्गस्य",
    hindiExplanation = "उपसर्ग पूर्वक अञ्चु धातु से समासान्त 'अ' प्रत्यय होता है (उदा. प्राङ्, प्राची)।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540070,
    role = SutraRole.Niyama,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        val last = context.padas.last().upadesha
        return (first == "प्र" || first == "परा" || first == "अप" || first == "सम") && last == "अञ्च्"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha } + "अ"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.70 adds Samāsānta 'a' suffix for Upasarga + añc compound '$compoundStem'.",
        )
    }
}
