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
 * Sūtra 5.4.148: त्रिचतुर्भ्यामुष्णोः (Ext registered as 5.4.148).
 * Extended rule for tri, catur before uṣṇa.
 */
object TricaturbhyamUsnohExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.148",
    text = "त्रिचतुर्भ्यामुष्णोः",
    hindiExplanation = "त्रि तथा चतुर् पूर्वपद से परे उष्ण शब्द से समासान्त प्रत्यय होता है।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540148,
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
        return (first == "त्रि" || first == "चतुर्") && last == "उष्ण"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha } + "क"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.148 adds Samāsānta after tri/catur + uṣṇa in '$compoundStem'.",
        )
    }
}
