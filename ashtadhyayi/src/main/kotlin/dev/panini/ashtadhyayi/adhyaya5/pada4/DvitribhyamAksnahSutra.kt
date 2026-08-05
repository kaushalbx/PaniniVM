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
 * Sūtra 5.4.137: द्वित्रिभ्यामक्ष्णः.
 * Samāsānta for akṣi after dvi or tri (dvyakṣam, tryakṣam).
 */
object DvitribhyamAksnahSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.137",
    text = "द्वित्रिभ्यामक्ष्णः",
    hindiExplanation = "द्वि तथा त्रि पूर्वपद से परे अक्षि शब्द से समासान्त प्रत्यय होता है (उदा. द्व्यक्षम्, त्र्यक्षम्)।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540137,
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
        return (first == "द्वि" || first == "त्रि") && (last == "अक्षि" || last == "अक्ष्")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val first = context.padas.first().upadesha
        val stem = if (first == "द्वि") "द्व्यक्ष" else "त्र्यक्ष"
        return SamasaRuleResult.Formed(
            compoundStem = stem,
            explanation = "5.4.137 adds Samāsānta after akṣi preceded by dvi/tri in '$stem'.",
        )
    }
}
