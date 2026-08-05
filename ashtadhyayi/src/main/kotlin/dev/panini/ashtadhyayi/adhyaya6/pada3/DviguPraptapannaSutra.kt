package dev.panini.ashtadhyayi.adhyaya6.pada3

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
 * Sūtra 6.3.16: द्विगुप्राप्तापन्नालंपूर्वगतिसमासेषु.
 * Aluk / Pūrvapada rules in Dvigu / Prāptāpanna compounds.
 */
object DviguPraptapannaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.16",
    text = "द्विगुप्राप्तापन्नालंपूर्वगतिसमासेषु",
    hindiExplanation = "द्विगु, प्राप्त, आपन्न, अलम् पूर्व तथा गति समास में उत्तरपद परे होने पर पूर्वपद नियम लागू होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630016,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.DVIGU,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        return context.samasaType == SamasaType.DVIGU || first == "प्राप्त" || first == "आपन्न" || first == "अलम्"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.16 applies pūrvapada rule for Dvigu/Prāptāpanna/Alam compound '$compoundStem'.",
        )
    }
}
