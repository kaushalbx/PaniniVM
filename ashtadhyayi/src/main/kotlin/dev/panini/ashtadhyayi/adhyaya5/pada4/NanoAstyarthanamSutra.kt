package dev.panini.ashtadhyayi.adhyaya5.pada4

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

import dev.panini.sutra.SamasaSutra

/**
 * 5.4.154: नञोऽस्त्यर्थानाम्.
 */
object NanoAstyarthanamSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.154",
    text = "नञोऽस्त्यर्थानाम्",
    hindiExplanation = "नञुत्तरपदादविद्यमानादिरूपान्नञोऽस्त्यर्थाच्च बहुव्रीहेः कप् स्यात्।",
    type = SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540154,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
), SamasaSutra {
    override val samasaType: SamasaType = SamasaType.BAHUVRIHI
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        return context.samasaType == SamasaType.BAHUVRIHI &&
            (purva == "अ" || purva == "अन" || purva == "अविद्यमान")
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val purva = context.purvaPada.upadesha
        val uttara = context.uttaraPada.upadesha
        val base = purva + uttara
        val compoundStem = base + "क"

        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "5.4.154: Added samāsānta kap-pratyaya after negative Bahuvrīhi ($compoundStem).",
        )
    }
}
