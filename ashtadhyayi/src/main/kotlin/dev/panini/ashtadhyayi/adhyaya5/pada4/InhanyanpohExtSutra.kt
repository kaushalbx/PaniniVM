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
 * Sūtra 5.4.159: इन्हन्यन्पोः (Ext registered as 5.4.159).
 * Extended rule for inhanyanpa.
 */
object InhanyanpohExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.159",
    text = "इन्हन्यन्पोः",
    hindiExplanation = "इन्, हन्, अन तथा अप् उत्तरपद से बहुव्रीहि में समासान्त प्रत्यय होता है।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540159,
    role = SutraRole.Niyama,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.BAHUVRIHI,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return last == "इन्" || last == "हन्" || last == "अन्" || last == "अप्"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha } + "क"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.159 adds Samāsānta after in/han/an/ap in '$compoundStem'.",
        )
    }
}
