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
 * Sūtra 5.4.90: उत्तमैकाभ्यां च.
 * Prescribes Samāsānta replacement after uttama and eka.
 */
object UttamaEkabhyamChaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.90",
    text = "उत्तमैकाभ्यां च",
    hindiExplanation = "उत्तम तथा एक के पश्चात् अहन् का अह्न आदेश होता है (उदा. उत्तमाह्णः)।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540090,
    role = SutraRole.Niyama,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = SamasaType.TATPURUSA,
    samasaPriority = 10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val first = context.padas.first().upadesha
        val last = context.padas.last().upadesha
        return last == "अहन्" && (first == "उत्तम" || first == "एक")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha } + "अह्न"
        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.90 applies ahna replacement after uttama/eka in '$compoundStem'.",
        )
    }
}
