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
 * Sūtra 5.4.165: स्वाङ्गाच्च (Ext registered as 5.4.165).
 * Extended body part rule.
 */
object SvangacChasExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.165",
    text = "स्वाङ्गाच्च",
    hindiExplanation = "स्वाङ्ग (शरीर का अङ्ग) वाचक उत्तरपद से बहुव्रीहि में समासान्त प्रत्यय होता है।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540165,
    role = SutraRole.Niyama,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.BAHUVRIHI,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return last == "मुख" || last == "नासिका" || last == "दन्त"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha } + "क"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.165 adds Samāsānta after body-part stem in '$compoundStem'.",
        )
    }
}
