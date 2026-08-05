package dev.panini.ashtadhyayi.adhyaya2.pada1

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.core.Vibhakti
import dev.panini.sutra.SamasaSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 2.1.38: अपेतापोढमुक्तपतितापत्रस्तैरल्पशः.
 *
 * Pañcamī nominals compound with 'apeta', 'apoḍha', 'mukta', 'patita', 'apatrasta' in Tatpuruṣa.
 */
object ApetaApodhaMuktaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.38",
    text = "अपेतापोढमुक्तपतितापत्रस्तैरल्पशः",
    hindiExplanation = "पञ्चम्यन्तं सुबन्तम् अपेतादिभिः सुबन्तैः सह समस्यते, सोऽपि तत्पुरुषः।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210038,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
), SamasaSutra {
    override val samasaType: SamasaType = SamasaType.TATPURUSA
    private val targetWords = setOf("अपेत", "अपोढ", "मुक्त", "पतित", "अपत्रस्त")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada
        val uttara = context.uttaraPada.upadesha
        return context.samasaType == SamasaType.TATPURUSA &&
            purva.vibhakti == Vibhakti.PANCHAMI &&
            uttara in targetWords
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }

        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.38: Formed Pañcamī Tatpuruṣa compound ($compoundStem).",
        )
    }
}
