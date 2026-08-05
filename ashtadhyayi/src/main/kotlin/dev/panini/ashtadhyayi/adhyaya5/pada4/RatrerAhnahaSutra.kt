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
 * Sūtra 5.4.96: रात्रे्रह्नसर्वैकदेशसंख्याताव्ययात्.
 * Prescribes Samāsānta replacing rātri with rātra after ahan, sarva, etc.
 * Example: सर्वनिरात्रः, सर्वरात्रः.
 */
object RatrerAhnahaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.96",
    text = "रात्रे्रह्नसर्वैकदेशसंख्याताव्ययात्",
    hindiExplanation = "अहन्, सर्व, एकदेश, संख्यात तथा अव्यय के पश्चात् रात्रि शब्द के स्थान पर 'रात्र' आदेश होता है (उदा. सर्वरात्रः)।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540096,
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
        return last == "रात्रि" && (first == "सर्व" || first == "अहन्" || first == "एकदेश" || first == "संख्यात")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val purva = context.padas.first().upadesha
        val compoundStem = purva + "रात्र"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.96 replaces rātri with rātra in '$compoundStem'.",
        )
    }
}
