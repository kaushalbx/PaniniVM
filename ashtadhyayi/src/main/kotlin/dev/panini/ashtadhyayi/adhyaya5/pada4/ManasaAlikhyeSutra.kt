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
 * Sūtra 5.4.103: मनस आलिख्ये.
 * Prescribes Samāsānta for manas in drawing/painting context.
 * Example: द्विमनसम् (dvimanasam).
 */
object ManasaAlikhyeSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.103",
    text = "मनस आलिख्ये",
    hindiExplanation = "आलेख्य (चित्र) विषय में मनस् उत्तरपद से समासान्त प्रत्यय होता है (उदा. द्विमनसम्)।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540103,
    role = SutraRole.Niyama,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.DVIGU,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return last == "मनस्"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha } + "अ"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.103 adds Samāsānta 'a' suffix for manas in '$compoundStem'.",
        )
    }
}
