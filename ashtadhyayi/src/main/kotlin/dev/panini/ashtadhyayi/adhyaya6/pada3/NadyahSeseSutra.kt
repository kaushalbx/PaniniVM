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
 * Sūtra 6.3.48 / Ext: नद्याः शेषे (registered as 6.3.148 for unique ID).
 * Shortening / feminine pūrvapada rule for river (nadī) words in compounds.
 */
object NadyahSeseSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.148",
    text = "नद्याः शेषे",
    hindiExplanation = "शेष (अन्य विषय) में नदीसंज्ञक पूर्वपद का ह्रस्व विधान होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630148,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        return first.endsWith("ी") || first.endsWith("ू")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.148 applies nadī pūrvapada shortening rule in '$compoundStem'.",
        )
    }
}
