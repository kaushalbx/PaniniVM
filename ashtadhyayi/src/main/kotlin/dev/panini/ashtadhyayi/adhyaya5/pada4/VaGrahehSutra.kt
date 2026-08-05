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
 * Sūtra 5.4.141: वा ग्रहेः.
 * Optional Samāsānta for grahi.
 */
object VaGrahehSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.141",
    text = "वा ग्रहेः",
    hindiExplanation = "ग्रह् धातु उत्तरपद से समासान्त प्रत्यय विकल्प से होता है।",
    type = SutraType.VIBHASHA,
    chapter = 5,
    pada = 4,
    optional = true,
    kramaValue = 540141,
    role = SutraRole.Niyama,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.BAHUVRIHI,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return last == "ग्रह" || last == "ग्राह"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha } + "अ"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.141 optionally adds Samāsānta for grahi in '$compoundStem'.",
        )
    }
}
