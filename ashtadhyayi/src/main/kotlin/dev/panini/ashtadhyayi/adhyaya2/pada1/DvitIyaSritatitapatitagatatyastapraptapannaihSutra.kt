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
 * 2.1.24: द्वितीया श्रितातीतपतितगतात्यस्तप्राप्तापन्नैः.
 *
 * Dvitiyā-subanta compounds with 'śrita', 'atīta', 'patita', 'gata', 'atyasta', 'prāpta', 'āpanna' in Tatpuruṣa.
 */
object DvitIyaSritatitapatitagatatyastapraptapannaihSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.24",
    text = "द्वितीया श्रितातीतपतितगतात्यस्तप्राप्तापन्नैः",
    hindiExplanation = "द्वितीयान्तः सुबन्तः श्रितादिभिः सुबन्तैः सह समस्यते, सोऽपि तत्पुरुषः।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210024,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
) {
    private val targetWords = setOf("श्रित", "अतीत", "पतित", "गत", "अत्यस्त", "प्राप्त", "आपन्न")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada
        val uttara = context.uttaraPada.upadesha
        return context.samasaType == SamasaType.TATPURUSA &&
            purva.vibhakti == Vibhakti.DVITIYA &&
            uttara in targetWords
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }

        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.24: Formed Dvitīyā Tatpuruṣa compound ($compoundStem).",
        )
    }
}
