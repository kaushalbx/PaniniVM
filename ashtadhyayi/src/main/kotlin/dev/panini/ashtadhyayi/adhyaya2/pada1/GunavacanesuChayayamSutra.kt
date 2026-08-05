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
 * Sūtra 2.1.5: गुणवचनेषु छायायाम्.
 * Prescribes Avyayībhāva compound with quality-denoting words in shade/reflection context.
 * Example: इक्षूणाम् छाया = इक्षुछाया / इक्षुछायम्.
 */
object GunavacanesuChayayamSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.5",
    text = "गुणवचनेषु छायायाम्",
    hindiExplanation = "छाया अर्थ में गुणवाचक पदों का समर्थ सुबन्त के साथ अव्ययीभाव समास होता है (उदा. इक्षुछायम्)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210005,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.AVYAYIBHAVA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val uttara = context.uttaraPada.upadesha
        return context.samasaType == SamasaType.AVYAYIBHAVA &&
            uttara.startsWith("छाया")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.5 forms Chāyā Avyayībhāva compound '$compoundStem'.",
        )
    }
}
