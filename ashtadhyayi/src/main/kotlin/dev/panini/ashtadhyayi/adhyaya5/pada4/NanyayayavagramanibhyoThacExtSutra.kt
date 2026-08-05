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
 * Sūtra 5.4.172: नन्यययवग्रामणीभ्यो ठच् (Ext registered as 5.4.272).
 * Extended rule for nanyayaya.
 */
object NanyayayavagramanibhyoThacExtSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.272",
    text = "नन्यययवग्रामणीभ्यो ठच्",
    hindiExplanation = "नन्य, यय, यव, ग्रामणी उत्तरपद से समासान्त ठच् (ठ -> इक) प्रत्यय होता है।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540272,
    role = SutraRole.Niyama,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.BAHUVRIHI,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return last == "नन्य" || last == "यय" || last == "यव" || last == "ग्रामणी"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha } + "इक"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.172 adds Samāsānta thac ('ika') in '$compoundStem'.",
        )
    }
}
