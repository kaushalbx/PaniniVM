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
 * Sūtra 5.4.84: जायाया ङिङ्.
 * Prescribes Samāsānta ṇiṅ suffix replacing jāyā with jāni in Bahuvrīhi.
 * Example: सीताजानिः (sītājāniḥ).
 */
object JayayaNingSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.84",
    text = "जायाया ङिङ्",
    hindiExplanation = "बहुव्रीहि समास में जाया उत्तरपद का समासान्त ङिङ् प्रत्यय होकर 'जानि' आदेश होता है (उदा. सीताजानिः)।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540084,
    role = SutraRole.Niyama,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.BAHUVRIHI,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return context.samasaType == SamasaType.BAHUVRIHI && last == "जाया"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val purva = context.padas.first().upadesha
        val compoundStem = purva + "जानि"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.84 applies Samāsānta ṇiṅ replacing jāyā with jāni in '$compoundStem'.",
        )
    }
}
