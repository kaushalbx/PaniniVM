package dev.panini.ashtadhyayi.adhyaya2.pada1

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.Vibhakti
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 2.1.24: द्वितीया श्रितातीतपतितगतात्यस्तप्राप्तापन्नैः.
 * Prescribes Dvitīyā Tatpuruṣa compound: pūrvapada must be in dvitīyā (accusative) case.
 * Matching: purely on purvaPadaVibhakti == DVITIYA — no surface-string check.
 */
object DvitiyaShritatitaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.24",
    text = "द्वितीया श्रितातीतपतितगतात्यस्तप्राप्तापन्नैः",
    hindiExplanation = "द्वितीयान्त समर्थ सुबन्त का श्रित, अतीत, पतित, गत आदि के साथ तत्पुरुष समास होता है (उदा. कृष्णश्रितः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210024,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    isGeneralFallback = true,
), dev.panini.sutra.SamasaSutra {
    // Authentic Pāṇinian condition: pūrvapada bears dvitīyā vibhakti
    override fun matches(context: SamasaRuleContext): Boolean =
        context.padas.size >= 2 && context.purvaPadaVibhakti == Vibhakti.DVITIYA

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val stem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = stem,
            explanation = "2.1.24 forms Dvitīyā Tatpuruṣa compound '$stem'.",
        )
    }
}
