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
 * Sūtra 2.1.20: खट्वा क्षेपे.
 * Prescribes Dvitīyā Tatpuruṣa compound when 'khaṭvā' compounds with a kta-participle in a deprecatory sense.
 * Example: खट्वाम् आरूढः = खट्वारूढः (khaṭvārūḍhaḥ - one who takes to bed prematurely/lazily).
 */
object KhatvaKsepeSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.20",
    text = "खट्वा क्षेपे",
    hindiExplanation = "निन्दा (क्षेप) अर्थ गम्यमान होने पर खट्वा शब्द का क्तान्त सुबन्त के साथ द्वितीय तत्पुरुष समास होता है (उदा. खट्वारूढः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210020,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        return context.samasaType == SamasaType.TATPURUSA &&
            purva == "खट्वा"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.20 forms Khaṭvā-kṣepa Dvitīyā Tatpuruṣa compound '$compoundStem'.",
        )
    }
}
