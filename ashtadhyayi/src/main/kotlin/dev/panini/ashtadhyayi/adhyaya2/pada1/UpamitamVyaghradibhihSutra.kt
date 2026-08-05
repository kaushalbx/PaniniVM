package dev.panini.ashtadhyayi.adhyaya2.pada1

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 2.1.56: उपमितं व्याघ्रादिभिः सामान्याप्रयोगे.
 * Prescribes Karmadhāraya compound of a noun representing object compared (Upamita)
 * with standard comparison words like 'vyāghra' (vyāghrādi gaṇa) when the common attribute is omitted.
 * Example: पुरुषः व्याघ्र इव = पुरुषव्याघ्रः.
 */
object UpamitamVyaghradibhihSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.56",
    text = "उपमितं व्याघ्रादिभिः सामान्याप्रयोगे",
    hindiExplanation = "उपमित (उपमेय) सुबन्त का व्याघ्र आदि उपमान सुबन्त के साथ कर्मधारय समास होता है (उदा. पुरुषव्याघ्रः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210056,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
) {
    private val vyaghradiGana = setOf("व्याघ्र", "सिंह", "ऋषभ", "चन्दना", "वृषभ", "नाग", "गज")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val uttara = context.uttaraPada.upadesha
        return uttara in vyaghradiGana
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val stem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = stem,
            explanation = "2.1.56 forms Upamita Karmadhāraya compound '$stem'.",
        )
    }
}
