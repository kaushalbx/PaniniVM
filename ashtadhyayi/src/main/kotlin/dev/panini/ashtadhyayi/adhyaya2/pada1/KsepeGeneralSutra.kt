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
 * Sūtra 2.1.48: क्षेपे (registered as 2.1.109 for unique ID).
 * Governance rule for Saptamī Tatpuruṣa compounds in reproach/censure.
 * Example: तीर्थे काकः = तीर्थकाकः (tīrthakākaḥ).
 */
object KsepeGeneralSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.109",
    text = "क्षेपे",
    hindiExplanation = "निन्दा (क्षेप) अर्थ में सप्तम्यन्त सुबन्त का समर्थ पद के साथ तत्पुरुष समास होता है (उदा. तीर्थकाकः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210109,
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
            explanation = "2.1.109 forms Reproach Saptamī Tatpuruṣa compound '$compoundStem'.",
        )
    }
}
