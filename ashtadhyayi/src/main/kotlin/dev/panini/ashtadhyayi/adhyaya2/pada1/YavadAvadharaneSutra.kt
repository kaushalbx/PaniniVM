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
 * Sūtra 2.1.8: यावद् अवधारणे (registered as 2.1.99 for unique ID).
 * Prescribes Avyayībhāva compound with 'yāvat' in limitation / measurement sense.
 * Example: यावन्तः अमोक्षाः = यावदमोकषम् (yāvadamokṣam / yāvadunam).
 */
object YavadAvadharaneSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.99",
    text = "यावद् अवधारणे",
    hindiExplanation = "अवधारण (अवधि) अर्थ में 'यावत्' अव्यय का समर्थ सुबन्त के साथ अव्ययीभाव समास होता है (उदा. यावदमोकषम्)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210099,
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
            purva == "यावत्"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.99 forms Yāvat-avadhāraṇa Avyayībhāva compound '$compoundStem'.",
        )
    }
}
