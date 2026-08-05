package dev.panini.ashtadhyayi.adhyaya2.pada2

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
 * Sūtra 2.2.39: चतुर्थी तदर्थार्थबलिहितसुखरक्षितैः (registered as 2.2.106 for unique ID).
 * Prescribes Caturthī Tatpuruṣa for intended material, benefit, offering, welfare, protection.
 * Example: यूपाय दारु = यूपदारु (yūpadāru), गोभ्यो हितम् = गोहितम् (gohitam).
 */
object CaturthiTadarthartheExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.106",
    text = "चतुर्थी तदर्थार्थबलिहितसुखरक्षितैः",
    hindiExplanation = "चतुर्थ्यन्त का तदर्थ (जिसके लिए कार्य हो), अर्थ, बलि, हित, सुख और रक्षित शब्दों के साथ तत्पुरुष समास होता है (उदा. यूपदारु, गोहितम्)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220106,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        return context.padas.size >= 2 && context.samasaType == SamasaType.TATPURUSA
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.2.106 forms Caturthī Tatpuruṣa compound '$compoundStem'.",
        )
    }
}
