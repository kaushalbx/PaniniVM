package dev.panini.ashtadhyayi.adhyaya5.pada4

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
 * Sūtra 5.4.131: संख्याया आसन्नाधिकसंख्याेषु.
 * Suffixes for approximate / exceeded numbers (e.g. āsannadaśāḥ).
 */
object SamkhyayaAsannadhikaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.131",
    text = "संख्याया आसन्नाधिकसंख्याेषु",
    hindiExplanation = "आसन्न, अधिक तथा संख्या बहुव्रीहि समास में 'बहुव्रीहावनुक्तोऽच्' नियम लागू होता है।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540131,
    role = SutraRole.Niyama,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.BAHUVRIHI,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        return first == "आसन्न" || first == "अधिक" || first == "उप"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha } + "अ"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.131 adds Samāsānta 'a' for numerical approximation in '$compoundStem'.",
        )
    }
}
