package dev.panini.ashtadhyayi.adhyaya2.pada1

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 2.1.18: पारैमध्ये षष्ठ्या वा.
 *
 * Compounds 'pāre' and 'madhye' with a 6th case nominal to form Avyayībhāva (e.g. 'pāregaṅgam', 'madhyegaṅgam').
 */
object PareMadhyeShashthyaVaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.18",
    text = "पारैमध्ये षष्ठ्या वा",
    hindiExplanation = "पारे मध्ये इत्येतौ षष्ठ्यन्तेन सह वा समस्येते, सोऽव्ययीभावः।",
    type = SutraType.VIBHASHA,
    chapter = 2,
    pada = 1,
    optional = true,
    kramaValue = 210018,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
) {
    private val pareMadhyeWords = setOf("पारे", "मध्ये", "पार", "मध्य")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        return purva in pareMadhyeWords
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val purva = context.purvaPada.upadesha
        val normalizedPurva = when (purva) {
            "पार" -> "पारे"
            "मध्य" -> "मध्ये"
            else -> purva
        }
        val compoundStem = normalizedPurva + context.uttaraPada.upadesha

        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.18: Formed Avyayībhāva compound with pāre/madhye ($compoundStem).",
        )
    }
}
