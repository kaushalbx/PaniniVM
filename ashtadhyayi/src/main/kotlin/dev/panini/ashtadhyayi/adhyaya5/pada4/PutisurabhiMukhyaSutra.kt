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
 * Sūtra 5.4.87: पूतिसुरभिमुखैभ्यः.
 * Prescribes Samāsānta i-pratyaya after gandha when preceded by pūti, surabhi, or mukhya.
 */
object PutisurabhiMukhyaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.87",
    text = "पूतिसुरभिमुखैभ्यः",
    hindiExplanation = "पूति, सुरभि तथा मुख्य पूर्व में होने पर गन्ध शब्द से समासान्त 'इ' प्रत्यय होता है।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540087,
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
        return last == "गन्ध" && (first == "पूति" || first == "सुरभि" || first == "मुख्य")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha } + "इ"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.87 adds Samāsānta 'i' suffix after pūti/surabhi/mukhya + gandha in '$compoundStem'.",
        )
    }
}
