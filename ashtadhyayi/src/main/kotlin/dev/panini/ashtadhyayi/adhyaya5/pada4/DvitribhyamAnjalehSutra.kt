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
 * Sūtra 5.4.102: द्वित्रीभ्यामञ्जलेः.
 * Prescribes Samāsānta -a suffix after añjali preceded by dvi or tri.
 * Example: द्व्यञ्जलिः (dvyañjaliḥ).
 */
object DvitribhyamAnjalehSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.102",
    text = "द्वित्रीभ्यामञ्जलेः",
    hindiExplanation = "द्वि तथा त्रि के पश्चात् अञ्जलि उत्तरपद से समासान्त प्रत्यय होता है (उदा. द्व्यञ्जलिः)।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540102,
    role = SutraRole.Niyama,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.DVIGU,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        val last = context.padas.last().upadesha
        return last == "अञ्जलि" && (first == "द्वि" || first == "त्रि")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha } + "अ"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.102 adds Samāsānta 'a' after dvi/tri + añjali in '$compoundStem'.",
        )
    }
}
