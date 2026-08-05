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
 * Sūtra 6.3.88: ऋतो विद्यायोनिसम्बन्धेभ्यः (Ext registered as 6.3.88).
 * Extended kinship & learning rule.
 */
object RtoVidyaYoniExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.88",
    text = "ऋतो विद्यायोनिसम्बन्धेभ्यः",
    hindiExplanation = "विद्या तथा योनि सम्बन्धवाचक ॠकारान्त शब्दों के पूर्वपद में आनङ् (आ) आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630088,
    role = SutraRole.Niyama,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.DVANDVA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        return first == "होतृ" || first == "पोतृ" || first == "पितामह"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.88 applies vidyā/yoni pūrvapada rule in '$compoundStem'.",
        )
    }
}
