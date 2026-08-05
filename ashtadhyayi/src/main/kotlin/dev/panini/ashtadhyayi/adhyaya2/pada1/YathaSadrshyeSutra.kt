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
 * Sūtra 2.1.7: यथाऽसादृश्ये.
 * Prescribes Avyayībhāva compound with 'yathā' when expressing non-similarity (yathārtha: yogyatā, vīpsā, padārthānativṛtti).
 * Example: शक्तिम् अनतिक्रम्य = यथाशक्ति (yathāśakti).
 */
object YathaSadrshyeSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.7",
    text = "यथाऽसादृश्ये",
    hindiExplanation = "असादृश्य (योग्यता, वीप्सा, पदार्थानतिवृत्ति) अर्थों में 'यथा' अव्यय का समर्थ सुबन्त के साथ अव्ययीभाव समास होता है (उदा. यथाशक्ति)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210007,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.AVYAYIBHAVA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        return context.samasaType == SamasaType.AVYAYIBHAVA &&
            purva == "यथा"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.7 forms Yathā Avyayībhāva compound '$compoundStem'.",
        )
    }
}
