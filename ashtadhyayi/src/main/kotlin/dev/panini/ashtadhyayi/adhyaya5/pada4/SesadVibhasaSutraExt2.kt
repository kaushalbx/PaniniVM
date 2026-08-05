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
 * Sūtra 5.4.173: शेषाद्विभाषा (Ext registered as 5.4.273).
 * Extended final optional Samāsānta rule.
 */
object SesadVibhasaSutraExt2 : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.273",
    text = "शेषाद्विभाषा",
    hindiExplanation = "शेष बहुव्रीहि समास से कप् समासान्त प्रत्यय विकल्प से होता है।",
    type = SutraType.VIBHASHA,
    chapter = 5,
    pada = 4,
    optional = true,
    kramaValue = 540273,
    role = SutraRole.Niyama,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.BAHUVRIHI,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        return context.samasaType == SamasaType.BAHUVRIHI
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha } + "क"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.173 optionally adds final Samāsānta kap ('ka') in '$compoundStem'.",
        )
    }
}
