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
 * Sūtra 6.3.32: मातरि पितरि च.
 * Pūrvapada rule for mātṛ and pitṛ in Devatā-dvandva.
 * Example: मातापितरौ (mātāpitarau), मातरापितरौ (mātarāpitarau).
 */
object MatariPitariChaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.32",
    text = "मातरि पितरि च",
    hindiExplanation = "माता तथा पिता द्वन्द्व समास में मातरापितरौ, मातापितरौ रूप निष्पन्न होते हैं।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630032,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.DVANDVA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        val last = context.padas.last().upadesha
        return (first == "मातृ" || first == "माता") && (last == "पितृ" || last == "पिता")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = "मातापितृ"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.32 forms mātāpitṛ in '$compoundStem'.",
        )
    }
}
