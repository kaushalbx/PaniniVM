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
 * 2.1.59: श्रेण्यादयः कृतादिभिः.
 *
 * Stems of Śreṇyādi gaṇa compound with kṛtādi words in Karmadhāraya.
 */
object SrenyadayahKrtadibhihSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.59",
    text = "श्रेण्यादयः कृतादिभिः",
    hindiExplanation = "श्रेण्यादयः सुबन्ताः कृतादिभिः सुबन्तैः सह समानाधिकरणेन समस्यन्ते, सोऽपि कर्मधारयः।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210059,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
), SamasaSutra {
    override val samasaType: SamasaType = SamasaType.KARMADHARAYA
    private val sreniWords = setOf("श्रेणि", "एक", "पूप", "पिण्ड", "गोल")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        return context.samasaType == SamasaType.KARMADHARAYA &&
            purva in sreniWords
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }

        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.59: Formed Śreṇyādi Karmadhāraya compound ($compoundStem).",
        )
    }
}
