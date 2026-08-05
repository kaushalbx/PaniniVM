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
 * Sūtra 5.4.162: नान्तादाः (Ext registered as 5.4.162).
 * Extended rule for n-ending stems.
 */
object NantadAhExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.162",
    text = "नान्तादाः",
    hindiExplanation = "नकारान्त उत्तरपद से बहुव्रीहि में समासान्त 'आ' (डा) प्रत्यय होता है।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540162,
    role = SutraRole.Niyama,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.BAHUVRIHI,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return last.endsWith("न्")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }.dropLast(1) + "आ"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.162 adds Samāsānta 'ā' for n-ending stem in '$compoundStem'.",
        )
    }
}
