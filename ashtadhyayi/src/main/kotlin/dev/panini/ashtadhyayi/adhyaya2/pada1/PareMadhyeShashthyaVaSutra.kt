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
 * 2.1.18: पारे मध्ये षष्ठ्या वा.
 *
 * Pāre and madhye optionally compound with a genitive (Ṣaṣṭhī) nominal in Avyayībhāva.
 */
object PareMadhyeShashthyaVaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.18",
    text = "पारे मध्ये षष्ठ्या वा",
    hindiExplanation = "पारे तथा मध्ये अव्यय षष्ठ्यन्त के साथ विकल्प से समस्यन्ते, सोऽव्ययीभावः।",
    type = SutraType.VIBHASHA,
    chapter = 2,
    pada = 1,
    optional = true,
    kramaValue = 210018,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
), SamasaSutra {
    override val samasaType: SamasaType = SamasaType.AVYAYIBHAVA
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
