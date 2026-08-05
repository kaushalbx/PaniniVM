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
 * Sūtra 5.4.106: त्रिचतुर्भ्यामुष्णोः.
 * Prescribes Samāsānta suffix after uṣṇa preceded by tri or catur.
 * Example: त्र्युष्णम् (tryuṣṇam).
 */
object TricaturbhyamUsnohSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.106",
    text = "त्रिचतुर्भ्यामुष्णोः",
    hindiExplanation = "त्रि तथा चतूर् पूर्व में होने पर उष्ण शब्द से समासान्त प्रत्यय होता है (उदा. त्र्युष्णम्)।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540106,
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
        return last == "उष्ण" && (first == "त्रि" || first == "चतुर्")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha } + "अ"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.106 adds Samāsānta 'a' after tri/catur + uṣṇa in '$compoundStem'.",
        )
    }
}
