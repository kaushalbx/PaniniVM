package dev.panini.ashtadhyayi.adhyaya2.pada1

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 2.1.37: पञ्चमी भयेन.
 * Prescribes Pañcamī Tatpuruṣa compound: pūrvapada must be in pañcamī (ablative) case.
 * Matching: purely on purvaPadaVibhakti == PANCHAMI — no surface-string check.
 */
object PancamiBhayenaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.37",
    text = "पञ्चमी भयेन",
    hindiExplanation = "पञ्चम्यन्त समर्थ सुबन्त का 'भय' शब्द के साथ पञ्चमी तत्पुरुष समास होता है (उदा. चोरभयम्)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210037,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    isGeneralFallback = true,
), dev.panini.sutra.SamasaSutra {
    // Authentic Pāṇinian condition: pūrvapada bears pañcamī vibhakti
    override fun matches(context: SamasaRuleContext): Boolean =
        context.padas.size >= 2 && context.purvaPadaVibhakti == Vibhakti.PANCHAMI

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val stem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = stem,
            explanation = "2.1.37 forms Pañcamī Tatpuruṣa compound '$stem'.",
        )
    }
}
