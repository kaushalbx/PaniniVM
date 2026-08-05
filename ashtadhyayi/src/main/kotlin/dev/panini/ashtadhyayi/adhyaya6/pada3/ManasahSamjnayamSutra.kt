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
 * Sūtra 6.3.8: मनसः संज्ञायाम्.
 * Prescribes Aluk of Saptamī vibhakti for manas in proper names.
 * Example: मनसिजः (manasijaḥ).
 */
object ManasahSamjnayamSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.8",
    text = "मनसः संज्ञायाम्",
    hindiExplanation = "संज्ञा (नाम) विषय में मनस् शब्द की सप्तमी विभक्ति का अलुक् होता है (उदा. मनसिजः)।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630008,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.ALUK_TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        return (context.samasaType == SamasaType.ALUK_TATPURUSA || context.samasaType == SamasaType.UPAPADA_TATPURUSA) &&
            (first == "मनस्" || first == "मनसि")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.8 preserves Saptamī case affix (Aluk) for manas in '$compoundStem'.",
        )
    }
}
