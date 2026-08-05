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
 * Sūtra 2.1.36: क्तेन (registered as 2.1.86 for unique ID).
 * Prescribes Tṛtīyā Tatpuruṣa compound with kta-ending verbal participle.
 * Example: अहिना हतः = अहिहतः (ahihataḥ).
 */
object KtenaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.86",
    text = "क्तेन",
    hindiExplanation = "तृतीयान्त समर्थ सुबन्त का क्तान्त सुबन्त के साथ तत्पुरुष समास होता है (उदा. अहिहतः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210086,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purvaVibhakti = context.purvaPadaVibhakti
        val uttara = context.uttaraPada.upadesha
        return context.samasaType == SamasaType.TATPURUSA &&
            purvaVibhakti == Vibhakti.TRTIYA &&
            (uttara == "हत" || uttara == "हतः")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.86 forms Kta Tṛtīyā Tatpuruṣa compound '$compoundStem'.",
        )
    }
}
