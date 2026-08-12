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
 * Sūtra 6.3.46: महतः समानाधिकरणजातीययोः.
 * Prescribes substitution of mahā for mahat before co-referential or jātīya terms.
 * Example: महाराजः (mahārājaḥ), महापुरुषः (mahāpuruṣaḥ).
 */
object MahatahSamanadhikaranaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.46",
    text = "महतः समानाधिकरणजातीययोः",
    hindiExplanation = "समानाधिकरण उत्तरपद तथा जातीयर् प्रत्यय परे होने पर महत् पूर्वपद को 'महा' (आकार) आदेश होता है (उदा. महाराजः, महापुरुषः)।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630046,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.KARMADHARAYA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        return (context.samasaType == SamasaType.KARMADHARAYA || context.samasaType == SamasaType.BAHUVRIHI || context.samasaType == SamasaType.TATPURUSA) &&
            (first == "महत्" || first == "महान्" || first == "महा")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val last = context.padas.last().upadesha
        val compoundStem = when {
            last.startsWith("आ") -> "महा" + last.drop(1)
            last.startsWith("अ") -> "महा" + last.drop(1)
            else -> "महा" + last
        }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.46 substitutes mahā for mahat in '$compoundStem'.",
        )
    }
}
