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
 * Sūtra 5.4.86: उपमानाच्च.
 * Prescribes Samāsānta i-pratyaya after gandha when preceded by an upamāna (comparison).
 * Example: पद्मगन्धिः (padmagandhiḥ).
 */
object UpamanacChaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.86",
    text = "उपमानाच्च",
    hindiExplanation = "उपमान वाची शब्द पूर्व में होने पर गन्ध शब्द से समासान्त 'इ' प्रत्यय होता है (उदा. पद्मगन्धिः)।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540086,
    role = SutraRole.Niyama,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.BAHUVRIHI,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return context.samasaType == SamasaType.BAHUVRIHI && last == "गन्ध"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha } + "इ"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.86 adds Samāsānta 'i' suffix after comparison gandha in '$compoundStem'.",
        )
    }
}
