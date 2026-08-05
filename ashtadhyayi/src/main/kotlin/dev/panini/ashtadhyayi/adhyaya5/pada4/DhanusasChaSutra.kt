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
 * Sūtra 5.4.83: धनुषश्च.
 * Prescribes Samāsānta a-pratyaya (aṅg) for compounds ending in dhanus.
 * Example: शार्ङ्गधन्वा (śārṅgadhanvā).
 */
object DhanusasChaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.83",
    text = "धनुषश्च",
    hindiExplanation = "धनुष् उत्तरपद वाले बहुव्रीहि तथा तत्पुरुष समास से समासान्त 'अ' (अङ्) प्रत्यय होता है (उदा. शार्ङ्गधन्वा)।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540083,
    role = SutraRole.Niyama,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.BAHUVRIHI,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val last = context.padas.last().upadesha
        return (context.samasaType == SamasaType.BAHUVRIHI || context.samasaType == SamasaType.TATPURUSA) && last == "धनुष्"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha } + "अ"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.83 adds Samāsānta 'a' suffix for dhanus in '$compoundStem'.",
        )
    }
}
