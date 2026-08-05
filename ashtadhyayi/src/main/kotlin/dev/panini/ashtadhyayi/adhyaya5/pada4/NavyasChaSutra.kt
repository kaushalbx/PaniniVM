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
 * Sūtra 5.4.99: नाव्याश्च.
 * Prescribes Samāsānta ac (-a) suffix after nau.
 * Example: अर्धनावः (ardhanāvaḥ).
 */
object NavyasChaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.99",
    text = "नाव्याश्च",
    hindiExplanation = "नौ उत्तरपद से समासान्त अच् (अ) प्रत्यय होकर आव् आदेश होता है (उदा. अर्धनावः)।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540099,
    role = SutraRole.Niyama,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return last == "नौ" || last == "नाव"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val purva = context.padas.first().upadesha
        val compoundStem = purva + "नाव"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.99 adds Samāsānta ac replacing nau with nāva in '$compoundStem'.",
        )
    }
}
