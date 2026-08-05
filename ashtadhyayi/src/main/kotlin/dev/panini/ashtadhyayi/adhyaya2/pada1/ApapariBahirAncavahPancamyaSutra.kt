package dev.panini.ashtadhyayi.adhyaya2.pada1

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
 * 2.1.12: अपपरिबहिरञ्चवः पञ्चम्या.
 *
 * Ap, pari, bahis, and words ending in añcu compound with a ablative (Pañcamī) subanta in Avyayībhāva.
 */
object ApapariBahirAncavahPancamyaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.12",
    text = "अपपरिबहिरञ्चवः पञ्चम्या",
    hindiExplanation = "अप, परि, बहिस् तथा अञ्चु-उत्तरपद अव्यय पञ्चम्यन्त के साथ समस्यन्ते, सोऽव्ययीभावः।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210012,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
), SamasaSutra {
    override val samasaType: SamasaType = SamasaType.AVYAYIBHAVA
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
