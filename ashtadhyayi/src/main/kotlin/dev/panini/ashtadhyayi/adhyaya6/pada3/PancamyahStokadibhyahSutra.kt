package dev.panini.ashtadhyayi.adhyaya6.pada3

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
 * Sūtra 6.3.2: पञ्चम्याः स्तोकादिभ्यः.
 * Prescribes Aluk (non-elision) of Pañcamī vibhakti after stokādi group.
 * Example: स्तोकान्मुक्तः (stokānmuktaḥ).
 */
object PancamyahStokadibhyahSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.2",
    text = "पञ्चम्याः स्तोकादिभ्यः",
    hindiExplanation = "स्तोकादि गण के शब्दों से परे पञ्चमी विभक्ति का अलुक् (अलोप) होता है (उदा. स्तोकान्मुक्तः)।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630002,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.ALUK_TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        return (context.purvaPadaVibhakti == Vibhakti.PANCHAMI || context.samasaType == SamasaType.ALUK_TATPURUSA) &&
            (first == "स्तोक" || first == "अन्तिक" || first == "अभ्याश" || first == "दूर")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.2 preserves Pañcamī case affix (Aluk) after stokādi in '$compoundStem'.",
        )
    }
}
