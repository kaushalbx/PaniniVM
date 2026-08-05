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
 * Sūtra 5.4.121: बहुव्रीहावनुक्तोऽच्.
 * Prescribes general Samāsānta -ac (-a) suffix in Bahuvrīhi when no specific suffix is prescribed.
 */
object BahuvrihavAnuktocSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.121",
    text = "बहुव्रीहावनुक्तोऽच्",
    hindiExplanation = "बहुव्रीहि समास में जहाँ विशेष समासान्त का अनुक्त (अकथन) हो, वहाँ अच् ('अ') प्रत्यय होता है।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540121,
    role = SutraRole.Niyama,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.BAHUVRIHI,
    samasaPriority = 1,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        return context.samasaType == SamasaType.BAHUVRIHI
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha } + "अ"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.121 adds general Samāsānta ac ('a') in Bahuvrīhi '$compoundStem'.",
        )
    }
}
