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
 * Sūtra 2.1.42: धार्क्ष्यात्.
 * Prescribes Saptamī Tatpuruṣa compound expressing boldness / audacity / arrogance.
 * Example: साङ्काश्ये जातः = साङ्काश्यकः.
 */
object DharksyatSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.42",
    text = "धार्क्ष्यात्",
    hindiExplanation = "धार्ष्ट्य (धृष्टता) अर्थ में सप्तम्यन्त का समर्थ सुबन्त के साथ तत्पुरुष समास होता है।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210042,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    private val dharksyaStems = setOf("साङ्काश्यक", "काम्पिल्यक", "माथुर")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val uttara = context.uttaraPada.upadesha
        return context.samasaType == SamasaType.TATPURUSA &&
            dharksyaStems.contains(uttara)
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.42 forms Dhārkṣya Saptamī Tatpuruṣa compound '$compoundStem'.",
        )
    }
}
