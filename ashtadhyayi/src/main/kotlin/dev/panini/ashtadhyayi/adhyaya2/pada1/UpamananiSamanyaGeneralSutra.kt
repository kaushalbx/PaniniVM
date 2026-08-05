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
 * Sūtra 2.1.53: उपमानानि सामान्यवचनैः (registered as 2.1.110 for unique ID).
 * Governance rule for comparison Karmadhāraya compounds.
 * Example: पुरुषः व्याघ्रः इव = पुरुषव्याघ्रः (puruṣavyāghraḥ).
 */
object UpamananiSamanyaGeneralSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.110",
    text = "उपमानानि सामान्यवचनैः",
    hindiExplanation = "उपमानवाचक सुबन्त का सामान्यवचन समर्थ सुबन्त के साथ कर्मधारय समास होता है।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210110,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.KARMADHARAYA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        return context.padas.size >= 2 && context.samasaType == SamasaType.KARMADHARAYA
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.110 forms Comparison Karmadhāraya compound '$compoundStem'.",
        )
    }
}
