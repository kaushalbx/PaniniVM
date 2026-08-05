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
 * Sūtra 5.4.80: किमनः क्षेपे.
 * Prescribes Samāsānta a-pratyaya after kim in censure/reproach.
 * Example: किम् राजा = किंसखा, किंवत्सरः.
 */
object KimanhKsepeSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.80",
    text = "किमनः क्षेपे",
    hindiExplanation = "क्षेप (कुत्सा) अर्थ में किम् शब्द से उत्तर समासान्त 'अ' (टच्) प्रत्यय होता है।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540080,
    role = SutraRole.Niyama,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        return first == "किम्"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha } + "अ"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.80 adds Samāsānta 'a' suffix after kim in censure '$compoundStem'.",
        )
    }
}
