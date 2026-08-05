package dev.panini.ashtadhyayi.adhyaya2.pada2

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
 * Sūtra 2.2.8: षष्ठी (registered as 2.2.93 for unique ID).
 * Prescribes general Ṣaṣṭhī Tatpuruṣa compound derivation.
 * Example: राज्ञः पुरुषः = राजपुरुषः (rājapuruṣaḥ).
 */
object ShashthiExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.93",
    text = "षष्ठी",
    hindiExplanation = "षष्ठ्यन्त सुबन्त का समर्थ सुबन्त के साथ तत्पुरुष समास होता है (उदा. राजपुरुषः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220093,
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
            explanation = "2.2.93 forms Ṣaṣṭhī Tatpuruṣa compound '$compoundStem'.",
        )
    }
}
