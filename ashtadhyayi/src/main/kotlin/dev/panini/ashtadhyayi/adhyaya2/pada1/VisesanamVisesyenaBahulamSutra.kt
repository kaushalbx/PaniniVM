package dev.panini.ashtadhyayi.adhyaya2.pada1

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 2.1.57: विशेषणं विशेष्येण बहुलम्.
 *
 * A modifying adjective compounds with a noun it modifies in same case (Karmadhāraya).
 */
object VisesanamVisesyenaBahulamSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.57",
    text = "विशेषणं विशेष्येण बहुलम्",
    hindiExplanation = "विशेषणं सुबन्तं विशेष्येण सुबन्तेन सह समानाधिकरणेन बहुलं समस्यते, सोऽपि कर्मधारयः।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210057,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
) {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        return context.samasaType == SamasaType.KARMADHARAYA
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }

        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.57: Formed Karmadhāraya compound ($compoundStem).",
        )
    }
}
