package dev.panini.ashtadhyayi.adhyaya2.pada1

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 2.1.12: अपपरिबहिरञ्चवः पञ्चम्या.
 *
 * Compounds 'apa', 'pari', 'bahir', and 'añcu'-ending words with a nominal in Pañcamī (5th case) to form Avyayībhāva.
 */
object ApapariBahirAncavahPancamyaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.12",
    text = "अपपरिबहिरञ्चवः पञ्चम्या",
    hindiExplanation = "अप, परि, बहिर् एते अव्ययाः अञ्चूत्तराश्च पञ्चम्यन्तेन सह समस्यन्ते, सोऽव्ययीभावः।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210012,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
) {
    private val apaPariWords = setOf("अप", "परि", "बहिर्", "बहिः", "प्राक्", "प्रत्याक्")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val purva = context.purvaPada.upadesha
        return purva in apaPariWords
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }

        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.12: Formed Avyayībhāva compound with apa/pari/bahir ($compoundStem).",
        )
    }
}
