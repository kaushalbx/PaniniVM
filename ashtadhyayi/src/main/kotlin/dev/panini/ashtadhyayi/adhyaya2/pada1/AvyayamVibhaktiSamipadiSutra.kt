package dev.panini.ashtadhyayi.adhyaya2.pada1

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 2.1.6: अव्ययं विभक्तिसमीपसमृद्धिव्यृद्ध्यर्थाभावात्ययासम्प्रतिशब्दप्रादुर्भावपश्चाद्यथाऽनुपूर्व्यायौगपद्यसादृश्यसम्पत्तिसाकल्यान्तवचनेषु.
 *
 * Indeclinable words in meanings of vibhakti, samīpa, samṛddhi, etc. compound in Avyayībhāva.
 */
object AvyayamVibhaktiSamipadiSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.6",
    text = "अव्ययं विभक्तिसमीपसमृद्धिव्यृद्ध्यर्थाभावात्ययासम्प्रतिशब्दप्रादुर्भावपश्चाद्यथाऽनुपूर्व्यायौगपद्यसादृश्यसम्पत्तिसाकल्यान्तवचनेषु",
    hindiExplanation = "अव्ययं विभक्त्याद्यर्थेषु वर्तमानं सुबन्तेन सह समस्यते, सोऽव्ययीभावः।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210006,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
) {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        return context.samasaType == SamasaType.AVYAYIBHAVA
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }

        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.6: Formed Avyayībhāva compound ($compoundStem).",
        )
    }
}
