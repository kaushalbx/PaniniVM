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
 * Sūtra 5.4.108: अङ्गुलं खाम्.
 * Prescribes Samāsānta suffix after aṅguli.
 * Example: द्व्यङ्गुलम् (dviaṅgulam).
 */
object AngulamKhamSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.108",
    text = "अङ्गुलं खाम्",
    hindiExplanation = "अङ्गुलि उत्तरपद से समासान्त प्रत्यय होता है (उदा. द्व्यङ्गुलम्)।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540108,
    role = SutraRole.Niyama,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.DVIGU,
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
            explanation = "5.4.108 adds Samāsānta 'a' after aṅguli in '$compoundStem'.",
        )
    }
}
