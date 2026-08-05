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
 * Sūtra 2.1.14: लक्षणेनाभिप्रती आभिमुख्ये (registered as 2.1.107 for unique ID).
 * Prescribes Avyayībhāva compound with abhi / prati indicating direction towards a sign.
 * Example: अग्निम् अभि = अभ्यग्नि (abhyagni), अग्निम् प्रति = प्रत्यग्नि (pratyagni).
 */
object LaksanenAbhipratiExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.107",
    text = "लक्षणेनाभिप्रती आभिमुख्ये",
    hindiExplanation = "आभिमुख्य अर्थ में अभि और प्रति अव्ययों का लक्षणवाचक सुबन्त के साथ अव्ययीभाव समास होता है (उदा. अभ्यग्नि, प्रत्यग्नि)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210107,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.AVYAYIBHAVA,
    samasaPriority = 10,
), SamasaSutra {
    private val prefixes = setOf("अभि", "प्रति")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        return context.samasaType == SamasaType.AVYAYIBHAVA && prefixes.contains(purva)
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.107 forms Abhi/Prati direction Avyayībhāva compound '$compoundStem'.",
        )
    }
}
