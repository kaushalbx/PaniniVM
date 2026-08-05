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
 * Sūtra 5.4.119: अङ्गुल्याः समासान्ताः.
 * Prescribes Samāsānta suffix rule after aṅguli.
 */
object AngulyahSamasantahSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.119",
    text = "अङ्गुल्याः समासान्ताः",
    hindiExplanation = "अङ्गुलि उत्तरपद से समासान्त प्रत्यय का विधान होता है।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540119,
    role = SutraRole.Niyama,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.BAHUVRIHI,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return last == "अङ्गुलि" || last == "अङ्गुल"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha } + "अ"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.119 adds Samāsānta suffix after aṅguli in '$compoundStem'.",
        )
    }
}
