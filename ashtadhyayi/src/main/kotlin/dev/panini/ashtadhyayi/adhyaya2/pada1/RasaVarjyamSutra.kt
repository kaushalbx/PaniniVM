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
 * Sūtra 2.1.44: रसवर्ज्यम्.
 * Prescribes Tṛtīyā Tatpuruṣa compound excluding taste-denoting (rasa) terms.
 * Example: धान्येन अर्थः = धान्यार्थः (dhānyārthaḥ).
 */
object RasaVarjyamSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.44",
    text = "रसवर्ज्यम्",
    hindiExplanation = "रस (रसवाचक पदों) को छोड़कर तृतीयान्त सुबन्त का समर्थ सुबन्त के साथ तत्पुरुष समास होता है (उदा. धान्यार्थः)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210044,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    private val rasaWords = setOf("मधुर", "अम्ल", "लवण", "कटुक", "तिक्त", "कषाय")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        val uttara = context.uttaraPada.upadesha
        return context.samasaType == SamasaType.TATPURUSA &&
            context.purvaPadaVibhakti == Vibhakti.TRTIYA &&
            (uttara == "अर्थ" || purva == "धान्य") &&
            !rasaWords.contains(purva)
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.44 forms Rasa-varjyam Tṛtīyā Tatpuruṣa compound '$compoundStem'.",
        )
    }
}
