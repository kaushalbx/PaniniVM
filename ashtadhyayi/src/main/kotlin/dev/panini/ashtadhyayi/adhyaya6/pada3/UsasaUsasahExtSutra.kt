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
 * Sūtra 6.3.85: उषस उषसः (Ext registered as 6.3.85).
 * Extended rule for uṣas.
 */
object UsasaUsasahExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.85",
    text = "उषस उषसः",
    hindiExplanation = "उषस् उत्तरपद परे होने पर उषस् पूर्वपद का आनङ्/दीर्घ आदेश नियम लागू होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630085,
    role = SutraRole.Niyama,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.DVANDVA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        val last = context.padas.last().upadesha
        return first == "उषस्" && last == "उषस्"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = "उषासोषा"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.85 derives uṣāsoṣā compound form.",
        )
    }
}
