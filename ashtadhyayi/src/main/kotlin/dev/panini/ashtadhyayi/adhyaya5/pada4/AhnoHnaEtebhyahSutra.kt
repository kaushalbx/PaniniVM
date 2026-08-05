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
 * Sūtra 5.4.88: अह्नोऽह्न एतेभ्यः.
 * Prescribes Samāsānta replacing ahan with ahna after sarva, eka, etc.
 * Example: सर्वाह्णः (sarvāhṇaḥ).
 */
object AhnoHnaEtebhyahSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.88",
    text = "अह्नोऽह्न एतेभ्यः",
    hindiExplanation = "सर्व, एक आदि शब्दों के पश्चात् अहन् उत्तरपद के स्थान पर 'अह्न' आदेश तथा समासान्त प्रत्यय होता है (उदा. सर्वाह्णः)।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540088,
    role = SutraRole.Niyama,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        val first = context.padas.first().upadesha
        return last == "अहन्" && (first == "सर्व" || first == "एक" || first == "पुण्य")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val purva = context.padas.first().upadesha
        val compoundStem = purva + "अह्न"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.88 replaces ahan with ahna after $purva in '$compoundStem'.",
        )
    }
}
