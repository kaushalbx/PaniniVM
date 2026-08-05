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
 * Sūtra 5.4.95: गिरिश्च सेनाकचच्छच्छायाशालानिशानाम्.
 * Prescribes Samāsānta suffix after giri, senā, chāyā, śālā, niśā.
 * Example: रामछायम् (rāmachāyam).
 */
object GiriscerSenaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.95",
    text = "गिरिश्च सेनाकचच्छच्छायाशालानिशानाम्",
    hindiExplanation = "गिरि, सेना, छाया, शाला, निशा उत्तरपद से समासान्त क्लीव/अ प्रत्यय होता है (उदा. रामछायम्)।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540095,
    role = SutraRole.Niyama,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return last == "गिरि" || last == "सेना" || last == "छाया" || last == "शाला" || last == "निशा"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha } + "अ"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.95 adds Samāsānta suffix in '$compoundStem'.",
        )
    }
}
