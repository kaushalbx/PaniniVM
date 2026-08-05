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
 * Sūtra 6.3.26: द्विस्त्रयोरिन्द्रे.
 * Pūrvapada lengthening for dvi and tri before indra in Devatā-dvandva.
 */
object DvistrayorIndreSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "6.3.26",
    text = "द्विस्त्रयोरिन्द्रे",
    hindiExplanation = "इन्द्र शब्द परे होने पर द्वि तथा त्रि शब्दों को आकार/दीर्घ आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630026,
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
        return last == "इन्द्र" && (first == "द्वि" || first == "त्रि")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "6.3.26 applies lengthening for dvi/tri before indra in '$compoundStem'.",
        )
    }
}
