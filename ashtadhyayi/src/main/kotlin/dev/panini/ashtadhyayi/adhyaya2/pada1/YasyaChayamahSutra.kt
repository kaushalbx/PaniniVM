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
 * Sūtra 2.1.10: यस्य चायामः.
 * Prescribes Avyayībhāva compound with 'anu' when expressing length or extent of a landmark.
 * Example: गङ्गायाः आयामः = अनुगङ्गम् (anugaṅgam).
 */
object YasyaChayamahSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.10",
    text = "यस्य चायामः",
    hindiExplanation = "जिसका आयाम (दैर्घ्य) गम्यमान हो, उस अर्थ में 'अनु' अव्यय का समर्थ सुबन्त के साथ अव्ययीभाव समास होता है (उदा. अनुगङ्गम्)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210010,
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
            purva == "अनु"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.10 forms Anu-āyāma Avyayībhāva compound '$compoundStem'.",
        )
    }
}
