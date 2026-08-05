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
 * Sūtra 6.3.27: सोमष्टुग्धान्येषु.
 * Pūrvapada augments before soma / dhānya.
 */
object SomasTugDhanyasuSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.27",
    text = "सोमष्टुग्धान्येषु",
    hindiExplanation = "धान्यवाचक सोम उत्तरपद परे होने पर ष्टुक् आगम होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630027,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return last == "सोम"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.27 applies ṭuk āgama before soma in '$compoundStem'.",
        )
    }
}
