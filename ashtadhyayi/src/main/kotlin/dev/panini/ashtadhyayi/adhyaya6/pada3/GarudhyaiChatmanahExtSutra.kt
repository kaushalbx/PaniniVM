package dev.panini.ashtadhyayi.adhyaya6.pada3

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
 * Sūtra 6.3.97: गरुध्यै चात्मनः (Ext registered as 6.3.97).
 * Extended rule for garudhyai/ātman.
 */
object GarudhyaiChatmanahExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.97",
    text = "गरुध्यै चात्मनः",
    hindiExplanation = "गरुध्यै विषय में आत्मन् पूर्वपद का रूप परिवर्तन नियम सिद्ध होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630097,
    role = SutraRole.Niyama,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        val last = context.padas.last().upadesha
        return (first == "आत्मन्" || first == "आत्मा") && last == "गरुध्यै"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.97 applies ātman transformation before garudhyai in '$compoundStem'.",
        )
    }
}
