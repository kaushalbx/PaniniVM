package dev.panini.ashtadhyayi.adhyaya2.pada1

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.core.SamasaType
import dev.panini.sutra.SamasaSutra
import dev.panini.sutra.Sutra

/**
 * 2.1.21: अन्यपदार्थे च.
 *
 * Number words compound with river names when expressing an external entity (anyapadārtha) in Avyayībhāva.
 */
object AnyapadartheChaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "2.1.21",
    text = "अन्यपदार्थे च",
    hindiExplanation = "अन्यपदार्थे वर्तमाना नदीभिः सह संख्या समस्यते, सोऽप्यव्ययीभावः।",
    type = SutraType.NITYA,
    chapter = 2,
    pada = 1,
    optional = false,
    kramaValue = 210021,
    role = SutraRole.Vidhi,
    action = SutraAction.VIDHI,
    scope = SutraScope.DERIVATION,
), SamasaSutra {
    override val samasaType: SamasaType = SamasaType.AVYAYIBHAVA
    private val riverWords = setOf("गङ्गा", "यमुना", "शोण", "नर्मदा", "सरयू")

    override fun matches(context: SamasaRuleContext): Boolean {
        if (context.padas.size < 2) return false
        val uttara = context.uttaraPada.upadesha
        return context.samasaType == SamasaType.AVYAYIBHAVA &&
            uttara in riverWords
    }

    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val compoundStem = context.padas.joinToString("") { it.upadesha }

        return SamasaRuleResult.Formed(
            compoundStem = compoundStem,
            explanation = "2.1.21: Formed Avyayībhāva river compound in anyapadārtha ($compoundStem).",
        )
    }
}
