package dev.panini.ashtadhyayi.adhyaya2.pada1

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
 * Sūtra 2.1.28: कालाः (registered as 2.1.108 for unique ID).
 * Governance rule for time-denoting words in Tatpuruṣa compounds.
 * Example: मासः प्रमितः अस्य = मासप्रमितः (māsapramitaḥ).
 */
object KalahGeneralSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.108",
    text = "कालाः",
    hindiExplanation = "कालवाचक सुबन्तों का समर्थ सुबन्त के साथ तत्पुरुष समास होता है।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210108,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        return context.padas.size >= 2 && context.samasaType == SamasaType.TATPURUSA
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.108 forms Time Tatpuruṣa compound '$compoundStem'.",
        )
    }
}
