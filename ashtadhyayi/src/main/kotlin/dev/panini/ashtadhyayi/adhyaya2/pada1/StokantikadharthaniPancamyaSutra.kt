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
 * 2.1.39: स्तोकन्तिकदूरार्थकृच्छ्राणि कृच्छ्रेण.
 *
 * Stoka, antika, dūra, and kṛcchra nominals in Pañcamī compound with kṛt-stems in Tatpuruṣa.
 */
object StokantikadharthaniPancamyaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.39",
    text = "स्तोकन्तिकदूरार्थकृच्छ्राणि कृच्छ्रेण",
    hindiExplanation = "स्तोक, अन्तिक, दूर तथा कृच्छ्र अर्थ वाले सुबन्ताः पञ्चम्यन्ताः कृतान्तेन सह समस्यन्ते, सोऽपि तत्पुरुषः।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210039,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
), SamasaSutra {
    private val targetWords = setOf("स्तोक", "अन्तिक", "दूर", "कृच्छ्र", "अल्प", "अभ्याश", "नेदिष्ठ", "विप्रकृष्ट")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada
        return context.samasaType == SamasaType.TATPURUSA &&
            purva.vibhakti == Vibhakti.PANCHAMI &&
            (purva.upadesha in targetWords || targetWords.any { purva.upadesha.contains(it) })
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }

        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.39: Formed Pañcamī Tatpuruṣa compound ($compoundStem).",
        )
    }
}
