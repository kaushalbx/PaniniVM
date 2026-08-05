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
 * Sūtra 2.1.36: चतुर्थी तदर्थार्थबलिहितसुखरक्षितैः.
 * Prescribes Caturthī Tatpuruṣa compound: pūrvapada must be in caturthī (dative) case.
 * Matching: purely on purvaPadaVibhakti == CHATURTHI.
 */
object CaturthiTadarthartheSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.36",
    text = "चतुर्थी तदर्थार्थबलिहितसुखरक्षितैः",
    hindiExplanation = "चतुर्थ्यन्त समर्थ सुबन्त का तदर्थ, अर्थ, बलि, हित, सुख और रक्षित शब्दों के साथ तत्पुरुष समास होता है (उदा. यूपदारु, गोहितम्)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210036,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
) {
    // Authentic Pāṇinian condition: pūrvapada bears caturthī vibhakti
    override fun matches(context: SamasaRuleContext): Boolean =
        context.padas.size >= 2 && context.purvaPadaVibhakti == Vibhakti.CHATURTHI

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val stem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = stem,
            explanation = "2.1.36 forms Caturthī Tatpuruṣa compound '$stem'.",
        )
    }
}
