package dev.panini.ashtadhyayi.adhyaya5.pada4

import dev.panini.core.SamasaRuleContext
import dev.panini.core.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope

/**
 * 5.4.151: उरःप्रभृतिभ्यः कप्च.
 *
 * Adds samāsānta pratyaya 'kap' ('ka') after stems of the uraḥprabhṛti group in a Bahuvrīhi compound.
 */
object UrahPrabhrtibhyahKapSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.151",
    text = "उरःप्रभृतिभ्यः कप्च",
    hindiExplanation = "उरःप्रभृतिभ्यः उत्तरपदभ्यो बहुव्रीहौ कप् प्रत्ययो भवति।",
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540151,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
) {
    private val urahPrabhrti = setOf("उरस्", "सर्पिस्", "पुमान्", "अनडुह्", "उरस्क")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val uttara = context.uttaraPada.upadesha
        return context.samasaType == SamasaType.BAHUVRIHI && (uttara in urahPrabhrti || uttara.endsWith("उरस्"))
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
            description = "5.4.151: Added samāsānta kap-pratyaya after uraḥprabhṛti stem ($compoundStem).",
        )
    }
}
