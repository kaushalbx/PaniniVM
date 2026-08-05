package dev.panini.ashtadhyayi.adhyaya6.pada3

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

import dev.panini.sutra.SamasaSutra

/**
 * Sūtra 6.3.1: अलुक उत्तरपदे.
 * Adhikāra Sūtra governing non-elision (aluk) of case markers of the prior member (pūrvapada)
 * when followed by an uttarapada in compounds.
 */
object AlukUttarapadeSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.1",
    text = "अलुक उत्तरपदे",
    hindiExplanation = "उत्तरपद परे होने पर पूर्वपद की विभक्ति का अलुक् (अलोप) होता है।",
    type = SutraType.ADHIKARA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630001,
    role = SutraRole.Adhikara(endKrama = 630037),
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
), SamasaSutra {
    override val samasaType: SamasaType = SamasaType.ALUK_TATPURUSA
    override val isGeneralFallback: Boolean = true
    override fun matches(context: SamasaRuleContext): Boolean =
        context.samasaType == SamasaType.ALUK_TATPURUSA && context.padas.size >= 2

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val stem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = stem,
            explanation = "6.3.1 (अलुक उत्तरपदे) preserves pūrvapada vibhakti for Aluk compound '$stem'.",
        )
    }
}
