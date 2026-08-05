package dev.panini.ashtadhyayi.adhyaya2.pada1

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 2.1.19: संख्या वंश्येन.
 *
 * Compounds a numeral (saṅkhyā) with a lineage/genealogical name (vaṁśya) to form Avyayībhāva (e.g. 'dvimuni', 'trimuni').
 */
object SankhyaVamsyenaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.19",
    text = "संख्या वंश्येन",
    hindiExplanation = "संख्या वंश्येन सह समस्यते, सोऽव्ययीभावः।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210019,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
) {
    private val numerals = setOf("एक", "द्वि", "त्रि", "चतुर्", "पञ्च", "षट्", "सप्त", "अष्ट", "नव", "दश")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        val uttara = context.uttaraPada.upadesha
        return purva in numerals || uttara == "मुनि" || uttara == "भारद्वाज"
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }

        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.19: Formed Avyayībhāva compound of numeral with lineage name ($compoundStem).",
        )
    }
}
