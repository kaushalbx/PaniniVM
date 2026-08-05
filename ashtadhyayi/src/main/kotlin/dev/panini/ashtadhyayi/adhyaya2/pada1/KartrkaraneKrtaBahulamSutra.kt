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
 * 2.1.32: कर्तृकरणे कृता बहुलम्.
 *
 * Tṛtīyā-subanta representing agent or instrument compounds with kṛt-affixed words in Tatpuruṣa.
 */
object KartrkaraneKrtaBahulamSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.32",
    text = "कर्तृकरणे कृता बहुलम्",
    hindiExplanation = "कर्तरि करणे च तृतीयान्तः सुबन्तः कृदन्तेन सह बहुलं समस्यते, सोऽपि तत्पुरुषः।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210032,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    isGeneralFallback = true,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada
        return context.samasaType == SamasaType.TATPURUSA &&
            purva.vibhakti == Vibhakti.TRTIYA
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }

        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.32: Formed Tṛtīyā Tatpuruṣa compound ($compoundStem).",
        )
    }
}
