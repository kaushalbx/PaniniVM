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
 * Sūtra 5.4.68: ऋक्पूरब्धूरपथामानक्शत्.
 * Prescribes Samāsānta a-pratyaya for compounds ending in ṛc, pur, ap, dhur, pathin.
 */
object RkPurAbDhurPathamSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.68",
    text = "ऋक्पूरब्धूरपथामानक्शत्",
    hindiExplanation = "ऋक्, पूर्, अप्, धूर् तथा पथिन् उत्तरपद वाले समासों से नित्य 'अ' (समासान्त) प्रत्यय होता है (उदा. अर्द्धर्चम्, विष्णुपुरम्)।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540068,
    role = SutraRole.Niyama,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return last == "ऋच्" || last == "पुर" || last == "अप्" || last == "धूर्" || last == "पथिन्"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha } + "अ"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.68 adds Samāsānta 'a' suffix for ṛc/pur/ap/dhur/pathin ending compound '$compoundStem'.",
        )
    }
}
