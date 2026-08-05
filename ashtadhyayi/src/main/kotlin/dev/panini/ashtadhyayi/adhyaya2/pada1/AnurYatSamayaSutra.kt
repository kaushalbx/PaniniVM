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
 * Sūtra 2.1.9: यावदवधारणे / अनुर् यत् समया.
 * Prescribes Avyayībhāva compound with 'anu' in proximity / alongside sense.
 * Example: वनस्य समीपे = अनुवनम् (anuvanam).
 */
object AnurYatSamayaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.9",
    text = "अनुर् यत् समया",
    hindiExplanation = "समीप अर्थ में 'अनु' अव्यय का समर्थ सुबन्त के साथ अव्ययीभाव समास होता है (उदा. अनुवनम्)।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210009,
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
            explanation = "2.1.9 forms Anu Avyayībhāva compound '$compoundStem'.",
        )
    }
}
