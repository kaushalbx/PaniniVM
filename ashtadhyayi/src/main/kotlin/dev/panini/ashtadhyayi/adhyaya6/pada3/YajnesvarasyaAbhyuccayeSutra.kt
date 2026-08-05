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
 * Sūtra 6.3.63: यज्ञेश्वरस्याभ्युच्चये.
 * Pūrvapada rule in sacrificial name context.
 */
object YajnesvarasyaAbhyuccayeSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.63",
    text = "यज्ञेश्वरस्याभ्युच्चये",
    hindiExplanation = "अभ्युच्चय (यज्ञ) अर्थ में ईश्वर शब्द पूर्वपद होने पर नियम लागू होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630063,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        return first == "यज्ञ" || first == "ईश्वर"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.63 applies sacrificial pūrvapada rule in '$compoundStem'.",
        )
    }
}
