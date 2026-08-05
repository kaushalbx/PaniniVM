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
 * Sūtra 6.3.56: हृदयस्य हृद्.
 * Substitution of hṛd for hṛdaya in compounds.
 * Example: सुहृद् (suhṛd), दुर्हृद् (durhṛd).
 */
object HrdayasyaHrdSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.56",
    text = "हृदयस्य हृद्",
    hindiExplanation = "हृदय शब्द के स्थान पर 'हृद्' आदेश होता है (उदा. सुहृद्, दुर्हृद्)।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630056,
    role = SutraRole.Niyama,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.BAHUVRIHI,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return last == "हृदय"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val first = context.padas.first().upadesha
        val compoundStem = first + "हृद्"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.56 substitutes hṛd for hṛdaya in '$compoundStem'.",
        )
    }
}
