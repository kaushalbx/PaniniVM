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
 * Sūtra 2.1.46: अपेक्षितम्.
 * Prescribes Saptamī Tatpuruṣa compound when the uttara-pada is 'apekṣita' (desired/expected).
 * Example: कार्ये अपेक्षितम् = कार्यापेक्षितम् (kāryāpekṣitam).
 */
object ApeksitamSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.46",
    text = "अपेक्षितम्",
    hindiExplanation = "सप्तम्यन्त सुबन्त का अपेक्षित शब्द के साथ तत्पुरुष समास होता है (उदा. कार्यापेक्षितम्)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210046,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val uttara = context.uttaraPada.upadesha
        return context.samasaType == SamasaType.TATPURUSA &&
            uttara == "अपेक्षित"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.46 forms Saptamī Tatpuruṣa compound '$compoundStem' with Apekṣita.",
        )
    }
}
