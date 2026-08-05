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
 * Sūtra 6.3.29: दिवाविजये.
 * Aluk for div in victory contexts (divā-vijayaḥ).
 */
object DivaVijayeSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.29",
    text = "दिवाविजये",
    hindiExplanation = "विजय अर्थ में दिव् शब्द की विभक्ति का अलुक् होता है (उदा. दिवाविजयः)।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630029,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.ALUK_TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        val last = context.padas.last().upadesha
        return (first == "दिव्" || first == "दिवा") && last == "विजय"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = "दिवाविजय"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.29 preserves Aluk for div before vijaya in '$compoundStem'.",
        )
    }
}
