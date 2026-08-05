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
 * Sūtra 5.4.167: प्रतंसमस्थादाः (Ext registered as 5.4.167).
 * Extended rule for pratamsa.
 */
object PratamsamasthadAhExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.167",
    text = "प्रतंसमस्थादाः",
    hindiExplanation = "प्रतम्, सम्, स्था उत्तरपद से समासान्त 'आ' (डा) प्रत्यय होता है।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540167,
    role = SutraRole.Niyama,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.BAHUVRIHI,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return last == "प्रतम्" || last == "सम्" || last == "स्था"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha } + "आ"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.167 adds Samāsānta 'ā' in '$compoundStem'.",
        )
    }
}
