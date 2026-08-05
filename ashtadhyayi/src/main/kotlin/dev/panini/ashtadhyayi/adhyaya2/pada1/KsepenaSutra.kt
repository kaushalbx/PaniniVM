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
 * Sūtra 2.1.43: क्षेपेण.
 * Prescribes Saptamī Tatpuruṣa compound when expressing censure/deprecation (kṣepa).
 * Example: गेहे क्षेडी = गेहेक्षेडी (gehekṣeḍī - lazy boasting at home).
 */
object KsepenaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.43",
    text = "क्षेपेण",
    hindiExplanation = "निन्दा (क्षेप) अर्थ में सप्तम्यन्त समर्थ सुबन्त का तत्पुरुष समास होता है (उदा. गेहेक्षेडी)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210043,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    private val ksepaStems = setOf("गेहेक्षेडी", "गेहेनर्द", "गेहेपर्द")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val stem = context.padas.joinToString("") { it.upadesha }
        return context.samasaType == SamasaType.TATPURUSA &&
            ksepaStems.contains(stem)
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.43 forms Saptamī Tatpuruṣa Kṣepa compound '$compoundStem'.",
        )
    }
}
