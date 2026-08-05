package dev.panini.ashtadhyayi.adhyaya5.pada4

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope

/**
 * 5.4.153: नद्यृतश्च.
 *
 * Adds samāsānta pratyaya 'kap' ('ka') after Nadī (ī/ū) or ṛ-ending stems in a Bahuvrīhi compound.
 */
object NadyrtaschaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.153",
    text = "नद्यृतश्च",
    hindiExplanation = "नद्यन्तादृदन्ताच्च बहुव्रीहेः कप् प्रत्ययो भवति।",
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540153,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
) {
    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val uttara = context.uttaraPada.upadesha
        return context.samasaType == SamasaType.BAHUVRIHI &&
            (uttara.endsWith("ी") || uttara.endsWith("ू") || uttara.endsWith("ऋ") || uttara.endsWith("ृ"))
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val purva = context.purvaPada.upadesha
        val uttara = context.uttaraPada.upadesha
        val base = purva + uttara
        val compoundStem = base + "क"

        return SamasaRuleResult.Formed(
            type = SamasaType.BAHUVRIHI,
            compoundStem = compoundStem,
            sutra = number,
            description = "5.4.153: Added samāsānta kap-pratyaya after Nadī/ṛ-ending stem ($compoundStem).",
        )
    }
}
