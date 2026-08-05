package dev.panini.ashtadhyayi.adhyaya2.pada1

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
 * Sūtra 2.1.67: युवा खलतिपलितवलितछत्रितैः.
 * Prescribes Karmadhāraya compound of 'yuvan' with words like khalati (bald), palita (grey), etc.
 * Example: युवा खलतिः = युवखलतिः.
 */
object YuvaKhalatiPalitaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.67",
    text = "युवा खलतिपलितवलितछत्रितैः",
    hindiExplanation = "युवन् शब्द का खलति, पलित, वलिन, वलित, छत्रित आदि शब्दों के साथ कर्मधारय समास होता है (उदा. युवखलतिः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210067,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.KARMADHARAYA,
    samasaPriority = 10,
), SamasaSutra {
    private val yuvanWords = setOf("युवन्", "युवा", "युव")
    private val targetAttributeWords = setOf("खलति", "पलित", "वलिन", "वलित", "छत्रित", "जरठ")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        val uttara = context.uttaraPada.upadesha
        return context.samasaType == SamasaType.KARMADHARAYA &&
            purva in yuvanWords && uttara in targetAttributeWords
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = "युव" + context.uttaraPada.upadesha
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.67 forms Yuvan Karmadhāraya compound '$compoundStem'.",
        )
    }
}
