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
 * Sūtra 5.4.74: ऋक्सामभ्यां ठच्.
 * Prescribes Samāsānta ṭhac affix after ṛc and sāman in Dvandva.
 * Example: ॠक्सामिकः (ṛksāmikaḥ).
 */
object RksamabhyamThacSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.74",
    text = "ऋक्सामभ्यां ठच्",
    hindiExplanation = "ऋक् तथा सामन् उत्तरपद वाले द्वन्द्व समास से समासान्त ठच् (इक) प्रत्यय होता है (उदा. ऋक्सामिकः)।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540074,
    role = SutraRole.Niyama,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.DVANDVA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return context.samasaType == SamasaType.DVANDVA && (last == "ऋच्" || last == "सामन्")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha } + "इक"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.74 adds Samāsānta ṭhac ('ika') suffix after ṛc/sāman in Dvandva '$compoundStem'.",
        )
    }
}
