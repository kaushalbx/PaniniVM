package dev.panini.ashtadhyayi.adhyaya2.pada2

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.sutra.SamasaSutra
import dev.panini.sutra.Sutra

/**
 * 2.2.2: अर्धं नपुंसकम्.
 *
 * Neuter stem 'ardha' (equal part) compounds with an ekadeśin (whole) in Tatpuruṣa.
 */
object ArdhamNapunsakamSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.2",
    text = "अर्धं नपुंसकम्",
    hindiExplanation = "क्लीवे साम्यवाचकम् अर्धशब्दः एकदेशिना सुबन्तेन सह समस्यते, सोऽपि तत्पुरुषः।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220002,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
), SamasaSutra {
    override val samasaType: SamasaType = SamasaType.TATPURUSA
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        return context.samasaType == SamasaType.TATPURUSA &&
            purva == "अर्ध"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }

        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.2.2: Formed Ardha Tatpuruṣa compound ($compoundStem).",
        )
    }
}
