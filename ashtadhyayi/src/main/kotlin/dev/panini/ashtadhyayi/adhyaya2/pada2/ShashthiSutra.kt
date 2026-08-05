package dev.panini.ashtadhyayi.adhyaya2.pada2

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
 * Sūtra 2.2.8: षष्ठी.
 * Prescribes Ṣaṣṭhī Tatpuruṣa compound: pūrvapada must be in ṣaṣṭhī (genitive) case.
 * Matching: purely on purvaPadaVibhakti == SASTHI.
 */
object ShashthiSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.8",
    text = "षष्ठी",
    hindiExplanation = "षष्ठ्यन्त समर्थ सुबन्त का समर्थ सुबन्त के साथ षष्ठी तत्पुरुष समास होता है (उदा. राजपुरुषः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220008,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    isGeneralFallback = true,
), dev.panini.sutra.SamasaSutra {
    // Authentic Pāṇinian condition: pūrvapada bears ṣaṣṭhī vibhakti (genitive)
    override fun matches(context: SamasaRuleContext): Boolean =
        context.padas.size >= 2 && context.purvaPadaVibhakti == Vibhakti.SASTHI

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val stem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = stem,
            explanation = "2.2.8 forms Ṣaṣṭhī Tatpuruṣa compound '$stem'.",
        )
    }
}
