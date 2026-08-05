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
 * Sūtra 6.3.41: स्त्रियाः पुंवद्भावः.
 * Prescribes masculine substitution (puṁvadbhāva) of feminine pūrvapada in compound.
 */
object StriyahPumvatbhavasSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.41",
    text = "स्त्रियाः पुंवद्भावः",
    hindiExplanation = "उत्तरपद परे होने पर स्त्रीलिङ्ग पूर्वपद का पुंवद्भाव (पुंल्लिङ्गवत् रूप) होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630041,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.KARMADHARAYA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        return context.samasaType == SamasaType.KARMADHARAYA || context.samasaType == SamasaType.BAHUVRIHI
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.41 applies puṁvadbhāva for feminine pūrvapada in '$compoundStem'.",
        )
    }
}
