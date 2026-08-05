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
 * Sūtra 6.3.25: त्रेः स्त्रियाम्.
 * Feminine substitution for tri in compounds (tisṛ).
 */
object TrehStriyamSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.25",
    text = "त्रेः स्त्रियाम्",
    hindiExplanation = "स्त्रीलिङ्ग अर्थ में त्रि शब्द के स्थान पर तिसृ आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630025,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.DVIGU,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        return first == "त्रि"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val last = context.padas.last().upadesha
        val compoundStem = "तिसृ" + last
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.25 applies feminine substitution tisṛ for tri in '$compoundStem'.",
        )
    }
}
