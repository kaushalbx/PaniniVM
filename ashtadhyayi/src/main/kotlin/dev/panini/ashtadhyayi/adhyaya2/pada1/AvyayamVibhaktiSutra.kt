package dev.panini.ashtadhyayi.adhyaya2.pada1

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.shiksha.Samjna
import dev.panini.sutra.SamasaSutra
import dev.panini.sutra.Sutra

/**
 * Sūtra 2.1.6: अव्ययं विभक्तिसमीपसमृद्धिव्यृद्ध्यर्थाभावात्ययासंप्रतिशब्दप्रादुर्भावपश्चाद्यथानुपूर्व्ययौगपद्यसादृश्यसंपत्तिसाकल्यान्तवचनेषु.
 * Prescribes Avyayībhāva compound formation between an Avyaya/Upasarga and a subanta.
 * Matching: pūrvapada must carry Samjna.AVYAYA or Samjna.UPASARGA — no surface-string check.
 */
object AvyayamVibhaktiSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.6",
    text = "अव्ययं विभक्तिसमीपसमृद्धिव्यृद्ध्यर्थाभावात्ययासंप्रतिशब्दप्रादुर्भावपश्चाद्यथानुपूर्व्ययौगपद्यसादृश्यसंपत्तिसाकल्यान्तवचनेषु",
    hindiExplanation = "विभक्ति, समीप आदि अर्थों में अव्यय का समर्थ सुबन्त के साथ अव्ययीभाव समास होता है (उदा. उपकृष्णम्, प्रतिगृहम्)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210006,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
), SamasaSutra {
    override val samasaType: SamasaType = SamasaType.AVYAYIBHAVA
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        // Authentic Pāṇinian condition: pūrvapada must carry AVYAYA or UPASARGA saṃjñā
        val purva = context.purvaPada
        return purva.samjnas.any { it == Samjna.AVYAYA || it == Samjna.UPASARGA }
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val stem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = stem,
            explanation = "2.1.6 forms Avyayībhāva compound '$stem'.",
        )
    }
}
