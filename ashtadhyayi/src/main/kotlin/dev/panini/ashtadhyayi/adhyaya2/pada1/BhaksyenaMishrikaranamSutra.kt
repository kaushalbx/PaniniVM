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
 * Sūtra 2.1.35: भक्ष्येण मिश्रीकरणम्.
 * Prescribes Tṛtīyā Tatpuruṣa compound when mixing food items (e.g. gudena mishrah = gudamishrah).
 */
object BhaksyenaMishrikaranamSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.35",
    text = "भक्ष्येण मिश्रीकरणम्",
    hindiExplanation = "भक्ष्यवाचक तृतीयान्त सुबन्त का मिश्रक शब्द के साथ तत्पुरुष समास होता है (उदा. गुडमिश्रः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210035,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    private val mixtureWords = setOf("मिश्र", "मिश्रित", "संसृष्ट")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val uttara = context.uttaraPada.upadesha
        return context.samasaType == SamasaType.TATPURUSA &&
            uttara in mixtureWords
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.35 forms Bhakṣya Tatpuruṣa compound '$compoundStem'.",
        )
    }
}
