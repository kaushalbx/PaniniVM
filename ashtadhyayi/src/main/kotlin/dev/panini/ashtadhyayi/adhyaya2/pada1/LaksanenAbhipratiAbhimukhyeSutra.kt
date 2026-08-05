package dev.panini.ashtadhyayi.adhyaya2.pada1

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 2.1.14: लक्षणेनाभिप्रती आभिमुख्ये.
 *
 * Compounds 'abhi' and 'prati' in facing/directional sense with a nominal indicator (lakṣaṇa),
 * placing the nominal first in Avyayībhāva (e.g. 'agnik' + 'abhi' -> 'agnyabhi').
 */
object LaksanenAbhipratiAbhimukhyeSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.14",
    text = "लक्षणेनाभिप्रती आभिमुख्ये",
    hindiExplanation = "आभिमुख्ये द्योत्ये अभि प्रति इत्येतौ लक्षणवाचिना सुबन्तेन समस्येते, सोऽव्ययीभावः।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210014,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
) {
    private val abhiPratiWords = setOf("अभि", "प्रति")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val padas = context.padas.map { it.upadesha }
        return padas.any { it in abhiPratiWords }
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val padas = context.padas.map { it.upadesha }
        val abhiOrPrati = padas.firstOrNull { it in abhiPratiWords } ?: "प्रति"
        val nominal = padas.firstOrNull { it !in abhiPratiWords } ?: padas.first()
        val compoundStem = nominal + abhiOrPrati

        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.14: Formed Avyayībhāva compound with postpositional abhi/prati ($compoundStem).",
        )
    }
}
