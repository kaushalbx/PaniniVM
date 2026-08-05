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

import dev.panini.sutra.SamasaSutra

/**
 * 2.1.41: सिद्धशुष्कपक्वबन्धैश्च.
 *
 * Saptamī-subanta compounds with 'siddha', 'śuṣka', 'pakva', 'bandha'/'baddha' in Tatpuruṣa.
 */
object SiddhasuskapakvabandhaishchaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.41",
    text = "सिद्धशुष्कपक्वबन्धैश्च",
    hindiExplanation = "सप्तम्यन्तः सुबन्तः सिद्ध-शुष्क-पक्व-बन्ध-शब्दैः सह समस्यते, सोऽपि तत्पुरुषः।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210041,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
), SamasaSutra {
    private val targetWords = setOf("सिद्ध", "शुष्क", "पक्व", "बन्ध", "बद्ध")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada
        val uttara = context.uttaraPada.upadesha
        return context.samasaType == SamasaType.TATPURUSA &&
            purva.vibhakti == Vibhakti.SAPTAMI &&
            uttara in targetWords
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }

        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.41: Formed Saptamī Tatpuruṣa compound ($compoundStem).",
        )
    }
}
