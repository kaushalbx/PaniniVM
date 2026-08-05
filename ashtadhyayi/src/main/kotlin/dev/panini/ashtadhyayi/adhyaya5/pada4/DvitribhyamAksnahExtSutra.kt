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
 * Sūtra 5.4.166: द्वित्रिभ्यामक्ष्णः (Ext registered as 5.4.166).
 * Extended rule for dvi/tri before akṣi.
 */
object DvitribhyamAksnahExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.166",
    text = "द्वित्रिभ्यामक्ष्णः",
    hindiExplanation = "द्वि तथा त्रि पूर्वपद से परे अक्षि उत्तरपद से षदच् प्रत्यय समासान्त होता है।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540166,
    role = SutraRole.Niyama,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.BAHUVRIHI,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        val last = context.padas.last().upadesha
        return (first == "द्वि" || first == "त्रि") && (last == "अक्षि" || last == "अक्ष")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha } + "अ"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.166 adds Samāsānta 'a' for dvi/tri + akṣi in '$compoundStem'.",
        )
    }
}
