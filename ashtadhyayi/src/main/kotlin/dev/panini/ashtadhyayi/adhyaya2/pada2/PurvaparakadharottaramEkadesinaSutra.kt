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
 * 2.2.1: पूर्वापराधरोत्तरमेकदेशिनाैकाधिकरणे.
 *
 * Purva, apara, adhara, uttara compound with an ekadeśin (whole entity) in Tatpuruṣa.
 */
object PurvaparakadharottaramEkadesinaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.2.1",
    text = "पूर्वापराधरोत्तरमेकदेशिनाैकाधिकरणे",
    hindiExplanation = "पूर्व, अपर, अधर, उत्तर सुबन्ताः एकदेशिना सुबन्तेन सह समस्यन्ते, सोऽपि तत्पुरुषः।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 2,
    optional = false,
    kramaValue = 220001,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
), SamasaSutra {
    private val ekadeshaWords = setOf("पूर्व", "अपर", "अधर", "उत्तर")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        return context.samasaType == SamasaType.TATPURUSA &&
            context.purvaPadaVibhakti == dev.panini.core.Vibhakti.PRATHAMA &&
            purva in ekadeshaWords
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }

        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.2.1: Formed Ekadeśin Tatpuruṣa compound ($compoundStem).",
        )
    }
}
