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
 * Sūtra 6.3.6: आत्मनश्च पूरणे (registered as 6.3.106 for unique ID).
 * Prescribes Aluk of case affix for ātman before ordinal words.
 * Example: आत्मनापञ्चमः (ātmanāpañcamaḥ).
 */
object AtmanascaPuraneExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.106",
    text = "आत्मनश्च पूरणे",
    hindiExplanation = "पूरण (संख्यापूरक) उत्तरपद परे होने पर आत्मन् शब्द से तृतीया विभक्ति का अलुक् होता है (उदा. आत्मनापञ्चमः)।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630106,
    role = SutraRole.Niyama,
    action = SutraAction.NIYAMA,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.ALUK_TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        return (context.samasaType == SamasaType.ALUK_TATPURUSA || context.samasaType == SamasaType.TATPURUSA) && (first == "आत्मन्" || first == "आत्मना")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.106 preserves Trtīyā case affix (Aluk) for ātman in '$compoundStem'.",
        )
    }
}
