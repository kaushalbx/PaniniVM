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
 * Sūtra 5.4.143: नन्यययवग्रामणीभ्यो ठच्.
 * Suffix thac after na, nya, yaya, gramani.
 */
object NanyayayavagramanibhyoThacSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.143",
    text = "नन्यययवग्रामणीभ्यो ठच्",
    hindiExplanation = "न, न्य, यय, ग्रामणी शब्दों से ठच् (ठ -> इक) प्रत्यय होता है।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540143,
    role = SutraRole.Niyama,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.BAHUVRIHI,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return last == "ग्रामणी" || last == "न" || last == "न्य"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha } + "क"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.143 adds Samāsānta thac ('ika'/'ka') in '$compoundStem'.",
        )
    }
}
