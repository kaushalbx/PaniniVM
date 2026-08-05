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
 * Sūtra 5.4.101: खार्याः प्राचाम्.
 * Eastern grammarians' Samāsānta rule for khārī.
 * Example: अर्धखारम् (ardhakhāram).
 */
object KharyahPracamSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.101",
    text = "खार्याः प्राचाम्",
    hindiExplanation = "प्राचाम् आचार्याणां मते खारी उत्तरपद से समासान्त प्रत्यय होता है (उदा. अर्धखारम्)।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540101,
    role = SutraRole.Niyama,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return last == "खारी" || last == "खार"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val purva = context.padas.first().upadesha
        val compoundStem = purva + "खार"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.101 applies Samāsānta for khārī in '$compoundStem'.",
        )
    }
}
