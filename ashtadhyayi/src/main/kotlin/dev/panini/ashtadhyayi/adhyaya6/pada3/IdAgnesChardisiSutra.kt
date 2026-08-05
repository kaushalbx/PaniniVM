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
 * Sūtra 6.3.28: इदग्नेश्चर्दिसि.
 * Long ī substitution for agni before chardis / soma (agnīṣomau).
 */
object IdAgnesChardisiSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.28",
    text = "इदग्नेश्चर्दिसि",
    hindiExplanation = "अग्नि शब्द को सोम/चर्दिसि परे होने पर ईकार (ईद्) आदेश होता है (उदा. अग्नीषोमौ)।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630028,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.DVANDVA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        val last = context.padas.last().upadesha
        return first == "अग्नि" && (last == "सोम" || last == "चर्दिस")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val last = context.padas.last().upadesha
        val compoundStem = "अग्नी" + last
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.28 applies long ī for agni in '$compoundStem'.",
        )
    }
}
